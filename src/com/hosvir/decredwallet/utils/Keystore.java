package com.hosvir.decredwallet.utils;

import com.hosvir.decredwallet.Constants;
import com.hosvir.decredwallet.TrustManagerDelegate;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.security.KeyStore;
import java.security.cert.Certificate;
import java.security.cert.CertificateFactory;

/**
 * @author fsig
 * @version 1.00
 * @since 20/03/17
 */
public class Keystore {
    private static KeyStore ks;
    private static X509TrustManager trustManager;

    /**
     * Create a blank Key store.
     *
     * @param keystore
     */
    public static void createNewKeystore(String keystore) {
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = Constants.getKeystorePassword().toCharArray();
            FileOutputStream fos = new FileOutputStream(keystore);
            ks.store(fos, password);
            fos.close();

            Thread.sleep(500);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Load the specified Key store.
     *
     * @param keystore
     */
    public static void loadKeystore(String keystore) {
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());
            char[] password = Constants.getKeystorePassword().toCharArray();
            FileInputStream fis = new FileInputStream(keystore);
            ks.load(fis, password);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Import the specified certificate into the Key store.
     *
     * @param certificate
     * @param certificateAlias
     * @param keystore
     * @return boolean
     */
    public static boolean importCertificate(String certificate, String certificateAlias, String keystore) {
        try {
            FileInputStream certIs = new FileInputStream(certificate);
            char[] password = Constants.getKeystorePassword().toCharArray();

            //Check if the certificate is already imported
            if (containsAlias(certificateAlias, keystore)) {
                certIs.close();
                return true;
            }

            BufferedInputStream bis = new BufferedInputStream(certIs);
            CertificateFactory cf = CertificateFactory.getInstance("X.509");
            while (bis.available() > 0) {
                Certificate cert = cf.generateCertificate(bis);
                ks.setCertificateEntry(certificateAlias, cert);
            }

            certIs.close();

            OutputStream out = new FileOutputStream(keystore);
            ks.store(out, password);
            out.close();

            return true;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Check if the specified Key store contains the specified certificate alias.
     *
     * @param certificateAlias
     * @param keystore
     * @return boolean
     */
    public static boolean containsAlias(String certificateAlias, String keystore) {
        try {
            ks = KeyStore.getInstance(KeyStore.getDefaultType());

            char[] password = Constants.getKeystorePassword().toCharArray();
            ks.load(null, password);

            FileInputStream fis = new FileInputStream(keystore);
            ks.load(fis, password);

            if (ks.containsAlias(certificateAlias)) {
                fis.close();
                return true;
            }

            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return false;
    }

    /**
     * Set the default trust manager to include the default keystore plus our keystore.
     */
    public static void setTrustManager() {
        try {
            //Trust manager
            TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init((KeyStore) null);

            //Get the default trust manager
            X509TrustManager defaultTm = null;
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    defaultTm = (X509TrustManager) tm;
                    break;
                }
            }

            tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            tmf.init(ks);

            //Get the default trust manager
            X509TrustManager myTm = null;
            for (TrustManager tm : tmf.getTrustManagers()) {
                if (tm instanceof X509TrustManager) {
                    myTm = (X509TrustManager) tm;
                    break;
                }
            }

            //Set trust manager
            trustManager = new TrustManagerDelegate(defaultTm, myTm);
            SSLContext sslContext = SSLContext.getInstance("TLS");
            sslContext.init(null, new TrustManager[]{trustManager}, null);
            SSLContext.setDefault(sslContext);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
