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
            return createTrustManagers(loadKeyStore(is));
        }
    }

    private KeyStore loadKeyStore(InputStream is) throws Exception {
        KeyStore keyStore = KeyStore.getInstance("JKS");
        char[] keyStorePassword = System.getProperty("keycloak.tls.truststore.password", "password").toCharArray();
        keyStore.load(is, keyStorePassword);
        return keyStore;
    }

    private TrustManager[] createTrustManagers(KeyStore keyStore) throws Exception {
        TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        trustManagerFactory.init(keyStore);
        return trustManagerFactory.getTrustManagers();
    }
}
