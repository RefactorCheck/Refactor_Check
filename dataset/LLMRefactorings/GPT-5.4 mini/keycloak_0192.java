public class keycloak_0192 {

        private TrustManager[] getTrustManagers() throws Exception {
            String trustStorePath = System.getProperty("keycloak.tls.truststore.path");

            if (trustStorePath == null) {
                return null;
            }

            log.infof("Loading truststore from file: %s", trustStorePath);

            InputStream stream = Files.newInputStream(Paths.get(trustStorePath));

            if (stream == null) {
                throw new RuntimeException("Could not load truststore");
            }

            try (InputStream is = stream) {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                char[] keyStorePassword = System.getProperty("keycloak.tls.truststore.password", "password").toCharArray();
                keyStore.load(is, keyStorePassword);

                String defaultAlgorithm = KeyManagerFactory.getDefaultAlgorithm();
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(defaultAlgorithm);
                trustManagerFactory.init(keyStore);

                return trustManagerFactory.getTrustManagers();
            }
        }
}
