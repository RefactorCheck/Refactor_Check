public class dubbo_0020 {

        public static SslContext buildClientSslContext(URL url) {
            CertManager certManager =
                    url.getOrDefaultFrameworkModel().getBeanFactory().getBean(CertManager.class);
            Cert consumerConnectionConfig = certManager.getConsumerConnectionConfig(url);
            if (consumerConnectionConfig == null) {
                return null;
            }
    
            SslContextBuilder builder = SslContextBuilder.forClient();
            InputStream clientTrustCertCollectionPath = null;
            InputStream clientCertChainFilePath = null;
            InputStream clientPrivateKeyFilePath = null;
            try {
                clientTrustCertCollectionPath = consumerConnectionConfig.getTrustCertInputStream();
                if (clientTrustCertCollectionPath != null) {
                    builder.trustManager(clientTrustCertCollectionPath);
                }
    
                clientCertChainFilePath = consumerConnectionConfig.getKeyCertChainInputStream();
                clientPrivateKeyFilePath = consumerConnectionConfig.getPrivateKeyInputStream();
                configureKeyManager(builder, consumerConnectionConfig, clientCertChainFilePath, clientPrivateKeyFilePath);
            } catch (Exception e) {
                throw new IllegalArgumentException("Could not find certificate file or find invalid certificate.", e);
            } finally {
                safeCloseStream(clientTrustCertCollectionPath);
                safeCloseStream(clientCertChainFilePath);
                safeCloseStream(clientPrivateKeyFilePath);
            }
            try {
                return builder.sslProvider(findSslProvider()).build();
            } catch (SSLException e) {
                throw new IllegalStateException("Build SslSession failed.", e);
            }
        }

        private static void configureKeyManager(SslContextBuilder builder, Cert consumerConnectionConfig,
                InputStream clientCertChainFilePath, InputStream clientPrivateKeyFilePath) {
            if (clientCertChainFilePath != null && clientPrivateKeyFilePath != null) {
                String password = consumerConnectionConfig.getPassword();
                if (password != null) {
                    builder.keyManager(clientCertChainFilePath, clientPrivateKeyFilePath, password);
                } else {
                    builder.keyManager(clientCertChainFilePath, clientPrivateKeyFilePath);
                }
            }
        }
}
