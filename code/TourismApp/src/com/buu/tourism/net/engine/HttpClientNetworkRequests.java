/**
 * 
 */
package com.buu.tourism.net.engine;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.zip.GZIPInputStream;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;

import com.buu.tourism.TourismApplication;
import com.buu.tourism.net.engine.HttpRequestEntity.RequestBody;

/**
 * {@link HttpURLConnection}的轻量级网络请求引擎
 * 
 * @author xingyu10
 *
 */
public class HttpClientNetworkRequests implements INetworkRequests {
    private static final int SOCKET_OPERATION_TIMEOUT = 60 * 1000;
    private static final int CONNECTION_TIMEOUT = 30 * 1000;
    private static final int BUFFER_SIZE = 16 * 1024;
    private static SSLSocketFactory sSSLSocketFactory;
    private int connTimeout = 0;
    private int readTimeout = 0;

    /**
     * 根据参数构建完整的URL格式
     * 
     * @param params
     * @return
     */
    private String buildCompleteUrl(String url, Bundle urlQuerys) {
        if (urlQuerys == null || urlQuerys.isEmpty()) {
            return url;
        }
        URI originalUri = URI.create(url);
        Uri.Builder uriBuilder = new Uri.Builder();
        uriBuilder.scheme(originalUri.getScheme());
        uriBuilder.encodedAuthority(originalUri.getRawAuthority());
        uriBuilder.encodedPath(originalUri.getRawPath());
        uriBuilder.encodedQuery(originalUri.getRawQuery());
        uriBuilder.encodedFragment(originalUri.getRawFragment());
        Object value = null;
        for (String key : urlQuerys.keySet()) {
            value = urlQuerys.get(key);
            if (value != null) {
                uriBuilder.appendQueryParameter(key, String.valueOf(value));
            }
        }
        return uriBuilder.build().toString();
    }

    /**
     * 计算请求的总大小(非精确计算)
     * 
     * @param url
     * @param type
     * @param header
     * @param contentLen
     * @return
     */
    long calculateRequestTotalLength(URL url, RequestType type, Map<String, String> header, long contentLen) {
        int urlLen = 0;
        int headerLen = 0;
        try {
            if (null != url) {
                StringBuilder statusLineBuilder = new StringBuilder();
                String path = url.getPath();
                path = (null == path) ? "" : path;
                String query = url.getQuery();
                query = (null == query) ? "" : query;
                statusLineBuilder.append(type.getMethod())//
                        .append(" ")//
                        .append(path).append(query)//
                        .append(" ")//
                        .append("HTTP/1.1")//
                        .append("\n");
                urlLen = statusLineBuilder.length();
            }
            if (null != header) {
                Set<Entry<String, String>> entrySet = header.entrySet();
                StringBuilder sb = new StringBuilder();
                if (null != entrySet) {
                    for (Entry<String, String> entry : entrySet) {
                        String key = entry.getKey();
                        key = (null == key) ? "" : key;
                        String value = entry.getValue();
                        value = (null == value) ? "" : value;
                        sb.append(key)//
                                .append(": ").append(value)//
                                .append("\n");
                    }
                    headerLen = sb.length();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        long totalLen = urlLen + headerLen + contentLen;
        return totalLen;
    }

    /**
     * 获取响应头的长度
     * 
     * @param responseHeader
     * @return
     */
    long getResponseHeaderLength(Map<String, List<String>> responseHeader) {
        long responseHeaderLen = 0;
        try {
            if (null != responseHeader) {
                Set<Entry<String, List<String>>> entrySet = responseHeader.entrySet();
                StringBuilder sb = new StringBuilder();
                if (null != entrySet) {
                    for (Entry<String, List<String>> entry : entrySet) {
                        String key = entry.getKey();
                        key = (null == key) ? "" : key;
                        List<String> values = entry.getValue();
                        String value = "";
                        if (null != values && values.size() > 0) {
                            value = values.get(0);
                            value = (null == value) ? "" : value;
                        }
                        sb.append(key)//
                                .append(": ")//
                                .append(value)//
                                .append("\n");
                    }
                }
                responseHeaderLen = sb.length();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return responseHeaderLen;
    }

    @Override
    public HttpResultEntity get(String url) {
        return request(new HttpRequestEntity(url));
    }

    @Override
    public HttpResultEntity get(String url, Bundle getParams) {
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(url);
        httpRequestEntity.setGetParams(getParams);
        return request(httpRequestEntity);
    }

    @Override
    public HttpResultEntity get(String url, HashMap<String, String> header, Bundle getParams) {
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(url);
        httpRequestEntity.setGetParams(getParams);
        httpRequestEntity.setHeader(header);
        return request(httpRequestEntity);
    }

    @Override
    public HttpResultEntity post(String url, InputStream postStream) {

        RequestBody requestBody = new RequestBody(postStream);
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(url);
        httpRequestEntity.setRequestBody(requestBody);
        httpRequestEntity.setType(RequestType.POST);
        return request(httpRequestEntity);
    }

    @Override
    public HttpResultEntity post(String url, HashMap<String, String> header, InputStream postStream) {

        RequestBody requestBody = new RequestBody(postStream);
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(url);
        httpRequestEntity.setHeader(header);
        httpRequestEntity.setRequestBody(requestBody);
        httpRequestEntity.setType(RequestType.POST);
        return request(httpRequestEntity);
    }

    @Override
    public HttpResultEntity request(HttpRequestEntity httpRequest) {
        return request(httpRequest, null);
    }

    @Override
    public HttpResultEntity request(HttpRequestEntity httpRequest, IHttpResponseCallBack callBack) {
        HttpResultEntity httpResult = new HttpResultEntity();
        /**
         * 是否强制获取字符串结果
         */
        boolean forceString = httpRequest.forceString;

        /**
         * 是否强制获取字节数组结果
         */
        boolean forceByteArr = httpRequest.forceByteArr;

        /**
         * 是否强制获取输出流
         */
        boolean forceInputStream = httpRequest.forceInputStream;

        /**
         * 
         * 是否自动处理重定向请求
         */
        boolean autoRedirect = httpRequest.autoRedirect;
        ;

        /**
         * 设置请求的url
         */
        String url = httpRequest.url;

        /**
         * 设置请求的header
         */
        Map<String, String> header = httpRequest.header;

        /**
         * 设置GET请求的参数
         */
        Bundle getParams = httpRequest.getParams;

        /**
         * 设置POST请求的输入流（网络层会读取该流，作为输出内容）
         */

        RequestBody requestBody = httpRequest.requestBody;

        /**
         * 设置请求的类型。GET/POST
         */
        RequestType type = httpRequest.type;

        /**
         * 设置请求的代理
         */
        Proxy proxy = httpRequest.proxy;

        HttpURLConnection urlConnection = null;
        if (null != callBack) {
            callBack = new UIHttpReqeuestCallBack(callBack);
        }
        try {
            if (null != getParams && getParams.size() > 0) {
                String newUrl = buildCompleteUrl(url, getParams);
                url = newUrl;
            }

            httpResult.requestTime = System.currentTimeMillis();
            httpResult.requestMethod = type.getMethod();
            httpResult.requestUrl = url;
            URL requestUrl = new URL(url);
            if (null != callBack) {
                callBack.onStart();
            }
            urlConnection = newHttpClient(requestUrl, proxy, type.getMethod(), autoRedirect);
            /**** 641新增自定义超时时间 *****/
            if (connTimeout > 0) {
                urlConnection.setConnectTimeout(connTimeout);
            }
            if (readTimeout > 0) {
                urlConnection.setReadTimeout(readTimeout);
            }
            /**************************/
            if (null != header && header.size() > 0) {
                for (Entry<String, String> entry : header.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpResult.requestHeader = new CaseInsensitiveMapVL(urlConnection.getRequestProperties());
            if (RequestType.POST.equals(type) && null != requestBody) {
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                // urlConnection.setChunkedStreamingMode(0);

                long totalLen = 0;
                DataOutputStream dos = null;

                InputStream postStream = requestBody;
                byte[] buffer = new byte[BUFFER_SIZE];
                int len = -1;
                dos = new DataOutputStream(urlConnection.getOutputStream());
                while ((len = postStream.read(buffer)) != -1) {
                    dos.write(buffer, 0, len);
                    dos.flush();
                    totalLen += len;
                }
                postStream.close();
                dos.close();
                httpResult.requestPostContentLength = totalLen;
            }
            httpResult.requestTotalLength = calculateRequestTotalLength(requestUrl, type, header, httpResult.requestPostContentLength);
            int responseCode = urlConnection.getResponseCode();
            httpResult.responseStatusCode = responseCode;
            httpResult.responseTime = System.currentTimeMillis();
            Map<String, List<String>> responseHeader = urlConnection.getHeaderFields();
            httpResult.responseHeader = new CaseInsensitiveMapVL(responseHeader);
            int responseContentLength = urlConnection.getContentLength();
            long responseHeaderLength = getResponseHeaderLength(httpResult.responseHeader);
            httpResult.responseContentLength = responseContentLength;
            httpResult.responseTotalLength = responseContentLength + responseHeaderLength;
            InputStream responseStream = urlConnection.getInputStream();
            ;
            // 判断如果是gzip压缩模式，如果是则转换该流
            List<String> list = httpResult.responseHeader.get("Content-Encoding");
            if (null != list && list.size() > 0) {
                String value = list.get(0);
                if (null != value && value.toLowerCase().indexOf("gzip") > -1) {
                    responseStream = new GZIPInputStream(responseStream);
                }
            }
            // 如果是强制获取输出流，则不再进行转换解析
            if (forceInputStream) {
                httpResult.responseInputStream = responseStream;
            } else {
                if (null != responseStream) {
                    // set default charset
                    String charset = "UTF-8";
                    // should convert inputstream to a string ？
                    boolean shouldConvert2Str = false;
                    List<String> contentTypes = httpResult.responseHeader.get("Content-Type");
                    if (null != contentTypes && contentTypes.size() > 0) {
                        try {
                            String contentType = contentTypes.get(0);
                            contentType = contentType.toLowerCase();
                            int index = -1;
                            String target = "charset=";
                            if ((index = contentType.indexOf(target)) != -1) {
                                charset = contentType.substring(index + target.length(), contentType.length());
                                charset = charset.trim();
                            }
                            shouldConvert2Str = (contentType.contains(target) || contentType.contains("text")
                                    || contentType.contains("json") || contentType.contains("xml"));
                        } catch (Exception e) {
                            e.printStackTrace();
                            charset = "UTF-8";
                        }
                    }

                    boolean shouldReadStream2Memory = shouldConvert2Str || forceString || forceByteArr;
                    boolean shouldCallBack = (null != callBack);
                    boolean shouldReadStream = shouldReadStream2Memory || shouldCallBack;
                    /**
                     * 判断是否应该读取响应流
                     */
                    if (shouldReadStream) {
                        byte[] buffer = new byte[BUFFER_SIZE];
                        int len = 0;
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        int contentLength = 0;
                        while ((len = responseStream.read(buffer)) != -1) {
                            if (shouldReadStream2Memory) {
                                byteArrayOutputStream.write(buffer, 0, len);
                            }
                            if (shouldCallBack) {
                                callBack.onUpdate(buffer, 0, len, responseContentLength);
                            }
                            contentLength += len;
                        }
                        // 这里再重新给响应内容大小 赋值
                        httpResult.responseContentLength = contentLength;
                        httpResult.responseTotalLength = responseHeaderLength + contentLength;
                        /**
                         * 强制获取原始数据
                         */
                        if (forceByteArr) {
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            httpResult.responseByteArray = byteArray;
                        }

                        /**
                         * 如果返回的内容本身就是TEXT格式 || 强制获取字符串形式结果
                         */
                        if (shouldConvert2Str || forceString) {
                            byte[] byteArray = byteArrayOutputStream.toByteArray();
                            httpResult.responseStr = new String(byteArray, charset);
                        }
                        byteArrayOutputStream.close();
                    }
                }
            }
            httpResult.finishTime = System.currentTimeMillis();

        } catch (Exception e) {
            httpResult.exception = e;
        } catch (OutOfMemoryError e) {
            httpResult.exception = e;
        } finally {
            if (null != urlConnection && !forceInputStream) {
                urlConnection.disconnect();
            }
            if (null != callBack) {
                callBack.onResult(httpResult);
            }
        }
        return httpResult;
    }

    /**
     * 主线程回调器
     * 
     * @author xingyu10
     *
     */
    static class UIHttpReqeuestCallBack implements IHttpResponseCallBack {
        private static final Handler mUIHandler = new Handler(Looper.getMainLooper());
        IHttpResponseCallBack realCallBack;

        public UIHttpReqeuestCallBack(IHttpResponseCallBack realCallBack) {
            this.realCallBack = realCallBack;
        }

        @Override
        public void onStart() {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (null != realCallBack) {
                        realCallBack.onStart();
                    }
                }
            });
        }

        @Override
        public void onUpdate(final byte[] buffer, final int offset, final int length, final int totalLength) {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (null != realCallBack) {
                        realCallBack.onUpdate(buffer, offset, length, totalLength);
                    }
                }
            });
        }

        @Override
        public void onResult(final HttpResultEntity result) {
            mUIHandler.post(new Runnable() {
                @Override
                public void run() {
                    if (null != realCallBack) {
                        realCallBack.onResult(result);
                    }
                }
            });
        }
    }

    static Certificate loadWeiboCertificate(Context context, String fileName) throws CertificateException, IOException {
        CertificateFactory cf = CertificateFactory.getInstance("X.509");
        AssetManager assetManager = context.getAssets();
        InputStream certInput = new BufferedInputStream(assetManager.open(fileName));
        Certificate certificate;
        try {
            certificate = cf.generateCertificate(certInput);
        } finally {
            certInput.close();
        }
        return certificate;
    }

    static SSLSocketFactory getSSLSocketFactory(Context context) {
        if (sSSLSocketFactory == null) {
            try {
                sSSLSocketFactory = SSLContext.getDefault().getSocketFactory();
            } catch (NoSuchAlgorithmException e1) {
                e1.printStackTrace();
                sSSLSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
            }
        }
        return sSSLSocketFactory;
    }

    /**
     * 创建一个urlconnection实例
     * 
     * @param requestUrl
     * @param proxy
     * @param method
     * @param autoRedirect
     * @return
     * @throws Exception
     */
    public static HttpURLConnection newHttpClient(URL requestUrl, Proxy proxy, String method, boolean autoRedirect) throws Exception {
        HttpURLConnection urlConnection;
        if (null != proxy) {
            urlConnection = (HttpURLConnection) requestUrl.openConnection(proxy);
        } else {
            urlConnection = (HttpURLConnection) requestUrl.openConnection();
        }
        if ("https".equalsIgnoreCase(requestUrl.getProtocol())) {
            HttpsURLConnection cons = (HttpsURLConnection) urlConnection;
            cons.setSSLSocketFactory(getSSLSocketFactory(TourismApplication.getInstance()));
        }
        urlConnection.setRequestMethod(method);
        urlConnection.setDoInput(true);

        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(SOCKET_OPERATION_TIMEOUT);
        urlConnection.setInstanceFollowRedirects(autoRedirect);
        return urlConnection;
    }

    /**
     * 设置Http请求链接超时时间
     */
    public void setConnectionTimeout(int conn_timeout) {
        connTimeout = conn_timeout;
    }

    /**
     * 设置读取Http请求响应流超时时间
     */
    public void setConnectionReadTimeout(int read_timeout) {
        readTimeout = read_timeout;
    }
}
