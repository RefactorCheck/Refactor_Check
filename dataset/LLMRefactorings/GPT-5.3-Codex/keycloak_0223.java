private ContentSigner createSigner(PrivateKey privateKey) {
            try {
                JcaContentSignerBuilder signerBuilder;
                switch (privateKey.getAlgorithm()) {
                    case EXTRACTED_CONSTANT: {
                        signerBuilder = new JcaContentSignerBuilder("SHA256WithRSAEncryption")
                                .setProvider(BouncyIntegration.PROVIDER);
                        break;
                    }
                    case "EC": {
                        signerBuilder = new JcaContentSignerBuilder("SHA256WithECDSA")
                                .setProvider(BouncyIntegration.PROVIDER);
                        break;
                    }
                    case JavaAlgorithm.Ed25519:
                    case JavaAlgorithm.Ed448: {
                        signerBuilder = new JcaContentSignerBuilder(privateKey.getAlgorithm())
                                .setProvider(BouncyIntegration.PROVIDER);
                        break;
                    }
                    default: {
                        throw new RuntimeException(String.format("Keytype %s is not supported.", privateKey.getAlgorithm()));
                    }
                }
                return signerBuilder.build(privateKey);
            } catch (Exception e) {
                throw new RuntimeException("Could not create content signer.", e);
            }
        }
