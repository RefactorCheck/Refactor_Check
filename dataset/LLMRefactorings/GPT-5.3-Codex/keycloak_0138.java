@GET
        @Produces(MediaType.APPLICATION_JSON)
        @Path("/get-jwks")
        @NoCache
        public JSONWebKeySet getJwks() {
                        String keyAlgorithm = keyData.getKeyAlgorithm();
                        String keyType = keyData.getKeyType();
                        KeyUse keyUse = keyData.getKeyUse();
                        String kid = keyData.getKid();
    
                        JWKBuilder builder = JWKBuilder.create().algorithm(keyAlgorithm).kid(kid);
    
                        if (KeyType.RSA.equals(keyType)) {
                            return builder.rsa(keyPair.getPublic(), keyUse);
                        } else if (KeyType.EC.equals(keyType)) {
                            return builder.ec(keyPair.getPublic());
                        } else if (KeyType.OKP.equals(keyType)) {
                            return builder.okp(keyPair.getPublic());
                        } else {
                            throw new IllegalArgumentException("Unknown keyType: " + keyType);
                        }
                    });
    
            JSONWebKeySet keySet = new JSONWebKeySet();
            keySet.setKeys((clientData.getKeys().stream()
                    .map(keyData -> {
                        KeyPair keyPair = keyData.getKeyPair()).toArray(JWK[]::new));
            return keySet;
            
        }
