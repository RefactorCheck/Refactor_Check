public class nacos_0055 {

        private static final String KEYSTORE_TYPE = "JKS";
        private static final String CERTIFICATE_TYPE = "X.509";
        private static final String CERTIFICATE_ENTRY_PREFIX = "cert-";

        private static TrustManager[] buildSecureTrustManager(String trustCertPath)
            throws SSLException {
            TrustManagerFactory selfTmf;
            InputStream in = null;
            
            try {
                String algorithm = TrustManagerFactory.getDefaultAlgorithm();
                selfTmf = TrustManagerFactory.getInstance(algorithm);
                
                KeyStore trustKeyStore = KeyStore.getInstance(KEYSTORE_TYPE);
                trustKeyStore.load(null, null);
                
                in = new FileInputStream(trustCertPath);
                CertificateFactory cf = CertificateFactory.getInstance(CERTIFICATE_TYPE);
                
                Collection<X509Certificate> certs =
                    (Collection<X509Certificate>) cf.generateCertificates(in);
                int count = 0;
                for (Certificate cert : certs) {
                    trustKeyStore.setCertificateEntry(CERTIFICATE_ENTRY_PREFIX + (count++), cert);
                }
                
                selfTmf.init(trustKeyStore);
                return selfTmf.getTrustManagers();
            } catch (Exception e) {
                LOGGER.error("build client trustManagerFactory failed", e);
                throw new SSLException(e);
            } finally {
                IoUtils.closeQuietly(in);
            }
        }
}
