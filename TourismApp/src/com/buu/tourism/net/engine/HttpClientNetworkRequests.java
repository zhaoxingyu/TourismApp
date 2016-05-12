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
import java.security.KeyStore;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;

import android.content.Context;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;

import com.buu.tourism.TourismApplication;
import com.buu.tourism.net.engine.HttpRequestEntity.MultiPartBody;
import com.buu.tourism.net.engine.HttpRequestEntity.RequestBody;
import com.buu.tourism.net.engine.content.ContentBody;

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
    public long calculateRequestTotalLength(URL url, RequestType type, Map<String, String> header, long contentLen) {
        int urlLen = type.getMethod().length() + 1 + url.toString().length() + 1 + "HTTP/1.1".length();
        int headerLen = 0;
        if (null != header) {
            Set<Entry<String, String>> entrySet = header.entrySet();
            for (Entry<String, String> entry : entrySet) {
                headerLen = (headerLen + 1 + entry.getKey().length() + entry.getValue().length());
            }
        }
        return urlLen + headerLen + contentLen;
    }

    /**
     * 将MutilPartBody写出到输入流
     * 
     * @param dos
     *            网络输出流
     * @param mutilPartBody
     * @param boundary
     * @return
     */
    long writeMultipartStream(DataOutputStream dos, MultiPartBody mutilPartBody, String boundary) throws Exception {
        long totalLen = 0;
        String end = "\r\n";
        String mark = "--";
        Set<Entry<String, ContentBody>> entrySet = mutilPartBody.entrySet();
        for (Entry<String, ContentBody> entry : entrySet) {
            String name = entry.getKey();
            ContentBody contentBody = entry.getValue();
            dos.writeBytes(mark + boundary + end);
            dos.writeBytes("Content-Disposition: form-data");
            dos.writeBytes(";name=" + "\"" + name + "\"");

            String fileName = contentBody.getFileName();
            if (!TextUtils.isEmpty(fileName)) {
                dos.writeBytes(";filename=" + "\"" + fileName + "\"");
            }
            dos.writeBytes(end);

            String contentType = contentBody.getMimeType();
            dos.writeBytes("Content-Type:" + "\"" + contentType + "\"");

            String charset = contentBody.getCharset();
            if (!TextUtils.isEmpty(charset)) {
                dos.writeBytes(";charset:" + "\"" + charset + "\"");
            }
            dos.writeBytes(end);

            dos.writeBytes("Content-Transfer-Encoding:" + "\"" + contentBody.getTransferEncoding() + "\"");

            dos.writeBytes(end);
            dos.writeBytes(end);

            InputStream postStream = contentBody.getPostStream();
            byte[] buffer = new byte[BUFFER_SIZE];
            int len = -1;

            while ((len = postStream.read(buffer)) != -1) {
                dos.write(buffer, 0, len);
                dos.flush();
                totalLen += len;
            }
            postStream.close();
            dos.writeBytes(end);
        }

        dos.writeBytes(mark + boundary + mark + end);
        dos.flush();
        return totalLen;
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
        RequestBody<InputStream> requestBody = new RequestBody<InputStream>();
        requestBody.setBody(postStream);
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(url);
        httpRequestEntity.setRequestBody(requestBody);
        return request(httpRequestEntity);
    }

    @Override
    public HttpResultEntity post(String url, HashMap<String, String> header, InputStream postStream) {
        RequestBody<InputStream> requestBody = new RequestBody<InputStream>();
        requestBody.setBody(postStream);
        HttpRequestEntity httpRequestEntity = new HttpRequestEntity(url);
        httpRequestEntity.setHeader(header);
        httpRequestEntity.setRequestBody(requestBody);
        return request(httpRequestEntity);
    }

    @Override
    public HttpResultEntity request(HttpRequestEntity httpRequest) {
        return request(httpRequest, null);
    }

    @Override
    public HttpResultEntity request(HttpRequestEntity httpRequest, IHttpResponseCallBack callBack) {
        return request(httpRequest, callBack, true, null);
    }

    @Override
    public HttpResultEntity request(HttpRequestEntity httpRequest, IHttpResponseCallBack callBack, boolean autoRedirect, Proxy proxy) {
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
        RequestBody<?> requestBody = httpRequest.requestBody;

        /**
         * 设置请求的类型。GET/POST
         */
        RequestType type = httpRequest.type;

        /**
         * 设置请求的回调
         */
        HttpURLConnection urlConnection = null;
        if (null != callBack) {
            callBack = new UIHttpReqeuestCallBack(callBack);
        }
        try {
            if (null != getParams && getParams.size() > 0) {
                String newUrl = buildCompleteUrl(url, getParams);
                url = newUrl;
            }
            httpResult.requestGetParams = getParams;
            httpResult.requestTime = System.currentTimeMillis();
            httpResult.requestMethod = type.getMethod();
            httpResult.requestUrl = url;
            URL requestUrl = new URL(url);
            if (null != callBack) {
                callBack.onStart();
            }
            urlConnection = newHttpClient(requestUrl, proxy, type.getMethod(), autoRedirect);

            if (null != header && header.size() > 0) {
                for (Entry<String, String> entry : header.entrySet()) {
                    urlConnection.setRequestProperty(entry.getKey(), entry.getValue());
                }
            }
            httpResult.requestHeader = urlConnection.getRequestProperties();
            if (null != requestBody) {
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.setChunkedStreamingMode(0);

                long totalLen = 0;
                DataOutputStream dos = null;

                Object body = requestBody.getBody();
                if (body instanceof InputStream) {
                    InputStream postStream = (InputStream) body;
                    byte[] buffer = new byte[BUFFER_SIZE];
                    int len = -1;
                    dos = new DataOutputStream(urlConnection.getOutputStream());
                    while ((len = postStream.read(buffer)) != -1) {
                        dos.write(buffer, 0, len);
                        dos.flush();
                        totalLen += len;
                    }
                    postStream.close();
                } else if (body instanceof MultiPartBody) {
                    MultiPartBody mutilPartBody = (MultiPartBody) body;
                    String boundary = "------------" + System.currentTimeMillis();
                    urlConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);
                    dos = new DataOutputStream(urlConnection.getOutputStream());
                    long totalSize = writeMultipartStream(dos, mutilPartBody, boundary);
                    totalLen = totalSize;
                }
                dos.close();
                httpResult.requestPostContentLength = totalLen;
            }
            httpResult.requestTotalLength = calculateRequestTotalLength(requestUrl, type, header, httpResult.requestPostContentLength);
            int responseCode = urlConnection.getResponseCode();
            httpResult.responseStatusCode = responseCode;
            httpResult.responseTime = System.currentTimeMillis();
            Map<String, List<String>> responseHeader = urlConnection.getHeaderFields();
            httpResult.responseHeader = responseHeader;
            int responseContentLength = urlConnection.getContentLength();
            long responseHeaderLength = getResponseHeaderLength(responseHeader);
            httpResult.responseContentLength = responseContentLength;
            httpResult.responseTotalLength = responseContentLength + responseHeaderLength;
            InputStream responseStream = urlConnection.getInputStream();

            // 如果是强制获取输出流，则不再进行转换解析
            if (forceInputStream) {
                httpResult.responseInputStream = responseStream;
            } else {
                if (null != responseStream) {
                    // set default charset
                    String charset = "UTF-8";
                    // should convert inputstream to a string ？
                    boolean shouldConvert2Str = false;
                    if (null != responseHeader) {
                        List<String> contentTypes = responseHeader.get("Content-Type");
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
                        //这里再重新给响应内容大小 赋值
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
            if (null != urlConnection) {
                urlConnection.disconnect();
            }
            if (null != callBack) {
                callBack.onResult(httpResult);
            }
        }
        return httpResult;
    }

    /**
     * 获取响应头的长度
     * 
     * @param responseHeader
     * @return
     */
    private long getResponseHeaderLength(Map<String, List<String>> responseHeader) {
        long totalLen = 0;
        if (null != responseHeader) {
            Set<Entry<String, List<String>>> entrySet = responseHeader.entrySet();
            StringBuilder sb = new StringBuilder();
            if (null != entrySet) {
                for (Entry<String, List<String>> entry : entrySet) {
                    String key = entry.getKey();
                    List<String> values = entry.getValue();
                    if (null != values && values.size() > 0) {
                        sb.append(key)//
                                .append(":").append(values.get(0))//
                                .append("\r\n");
                    }
                }
            }
            totalLen = sb.length();
        }
        return totalLen;
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
        urlConnection.setRequestMethod(method);
        urlConnection.setDoInput(true);
        urlConnection.setConnectTimeout(CONNECTION_TIMEOUT);
        urlConnection.setReadTimeout(SOCKET_OPERATION_TIMEOUT);
        urlConnection.setInstanceFollowRedirects(autoRedirect);
        urlConnection.setRequestProperty("Connection", "keep-alive");
        return urlConnection;
    }
}
