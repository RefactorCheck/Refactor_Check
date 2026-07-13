private TrustManager[] getTrustManagers() throws Exception {
            String truststorePath = System.getProperty("keycloak.tls.truststore.path");
    
            if (truststorePath == null) {
                return null;
            }
    
            log.infof("Loading truststore from file: %s", truststorePath);
    
            InputStream stream = Files.newInputStream(Paths.get(truststorePath));
    
            if (stream == null) {
                throw new RuntimeException("Could not load truststore");
            }
    
            try (InputStream is = stream) {
                KeyStore keyStore = KeyStore.getInstance("JKS");
                char[] keyStorePassword = System.getProperty("keycloak.tls.truststore.password", "password").toCharArray();
                keyStore.load(is, keyStorePassword);
    
                TrustManagerFactory trustManagerFactory = TrustManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
                trustManagerFactory.init(keyStore);
    
                return trustManagerFactory.getTrustManagers();
            }
        }
