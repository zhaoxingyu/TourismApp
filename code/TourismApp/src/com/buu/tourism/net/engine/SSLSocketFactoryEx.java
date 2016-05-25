package com.buu.tourism.net.engine;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

public class SSLSocketFactoryEx extends javax.net.ssl.SSLSocketFactory {

    SSLContext sslContext = SSLContext.getInstance("TLS");

    public SSLSocketFactoryEx(KeyStore truststore) throws NoSuchAlgorithmException, KeyManagementException, KeyStoreException,
            UnrecoverableKeyException {
        sslContext.init(null, new TrustManager[] { new KeyStoresTrustManagerEX(truststore) }, null);
    }

    public static class KeyStoresTrustManagerEX implements X509TrustManager {

        protected ArrayList<X509TrustManager> x509TrustManagers = new ArrayList<X509TrustManager>();

        protected KeyStoresTrustManagerEX(KeyStore... additionalkeyStores) {
            final ArrayList<TrustManagerFactory> factories = new ArrayList<TrustManagerFactory>();
            try {
                // The default Trustmanager with default keystore
                final TrustManagerFactory original = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                original.init((KeyStore) null);
                factories.add(original);

                for (KeyStore keyStore : additionalkeyStores) {
                    final TrustManagerFactory additionalCerts = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
                    additionalCerts.init(keyStore);
                    factories.add(additionalCerts);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            /*
             * Iterate over the returned trustmanagers, and hold on to any that
             * are X509TrustManagers
             */
            for (TrustManagerFactory tmf : factories)
                for (TrustManager tm : tmf.getTrustManagers())
                    if (tm instanceof X509TrustManager)
                        x509TrustManagers.add((X509TrustManager) tm);

            if (x509TrustManagers.size() == 0) {
                throw new RuntimeException("Couldn't find any X509TrustManagers");
            }
        }

        /*
         * Delegate to the default trust manager.
         */
        @Override
        public void checkClientTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            final X509TrustManager defaultX509TrustManager = x509TrustManagers.get(0);
            defaultX509TrustManager.checkClientTrusted(chain, authType);
        }

        /*
         * Loop over the trustmanagers until we find one that accepts our server
         */
        @Override
        public void checkServerTrusted(X509Certificate[] chain, String authType) throws CertificateException {
            for (X509TrustManager tm : x509TrustManagers) {
                try {
                    tm.checkServerTrusted(chain, authType);
                    return;
                } catch (CertificateException e) {
                    // ignore
                }
            }
            throw new CertificateException();
        }

        @Override
        public X509Certificate[] getAcceptedIssuers() {
            final ArrayList<X509Certificate> list = new ArrayList<X509Certificate>();
            for (X509TrustManager tm : x509TrustManagers)
                list.addAll(Arrays.asList(tm.getAcceptedIssuers()));
            return list.toArray(new X509Certificate[list.size()]);
        }
    }

    @Override
    public String[] getDefaultCipherSuites() {
        return sslContext.getSocketFactory().getDefaultCipherSuites();
    }

    @Override
    public String[] getSupportedCipherSuites() {
        return sslContext.getSocketFactory().getSupportedCipherSuites();
    }

    @Override
    public Socket createSocket() throws IOException {
        return sslContext.getSocketFactory().createSocket();
    }

    @Override
    public Socket createSocket(String host, int port) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(String host, int port, InetAddress localHost, int localPort) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(host, port, localHost, localPort);
    }

    @Override
    public Socket createSocket(Socket socket, String host, int port, boolean autoClose) throws IOException, UnknownHostException {
        return sslContext.getSocketFactory().createSocket(socket, host, port, autoClose);
    }

    @Override
    public Socket createSocket(InetAddress host, int port) throws IOException {
        return sslContext.getSocketFactory().createSocket(host, port);
    }

    @Override
    public Socket createSocket(InetAddress address, int port, InetAddress localAddress, int localPort) throws IOException {
        return sslContext.getSocketFactory().createSocket(address, port, localAddress, localPort);
    }
}
