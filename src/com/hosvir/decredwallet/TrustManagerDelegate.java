package com.hosvir.decredwallet;

import javax.net.ssl.X509TrustManager;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

/**
 * @author captain-redbeard
 * @version 1.00
 * @since 3/04/17
 */
public class TrustManagerDelegate implements X509TrustManager {
    private final X509TrustManager serverTrustManager;
    private final X509TrustManager clientTrustManager;

    public TrustManagerDelegate(X509TrustManager serverTrustManager, X509TrustManager clientTrustManager) {
        this.serverTrustManager = serverTrustManager;
        this.clientTrustManager = clientTrustManager;
    }

    @Override
    public void checkClientTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        this.clientTrustManager.checkClientTrusted(chain, authType);
    }

    @Override
    public void checkServerTrusted(final X509Certificate[] chain, final String authType) throws CertificateException {
        try {
            serverTrustManager.checkServerTrusted(chain, authType);
        } catch (CertificateException ex) {
            this.serverTrustManager.checkServerTrusted(chain, authType);
        }
    }

    @Override
    public X509Certificate[] getAcceptedIssuers() {
        return this.clientTrustManager.getAcceptedIssuers();
    }

}