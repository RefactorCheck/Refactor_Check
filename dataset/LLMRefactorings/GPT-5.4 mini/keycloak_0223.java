public class keycloak_0223 {

        private static final String RSA_SIGNATURE_ALGORITHM = "SHA256WithRSAEncryption";
        private static final String EC_SIGNATURE_ALGORITHM = "SHA256WithECDSA";
        private static final String UNSUPPORTED_KEYTYPE_MESSAGE = "Keytype %s is not supported.";
        private static final String CONTENT_SIGNER_ERROR_MESSAGE = "Could not create content signer.";

        private ContentSigner createSigner(PrivateKey privateKey) {
            try {
                JcaContentSignerBuilder signerBuilder;
                switch (privateKey.getAlgorithm()) {
                    case "RSA": {
                        signerBuilder = new JcaContentSignerBuilder(RSA_SIGNATURE_ALGORITHM)
                                .setProvider(BouncyIntegration.PROVIDER);
                        break;
                    }
                    case "EC": {
                        signerBuilder = new JcaContentSignerBuilder(EC_SIGNATURE_ALGORITHM)
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
                        throw new RuntimeException(String.format(UNSUPPORTED_KEYTYPE_MESSAGE, privateKey.getAlgorithm()));
                    }
                }
                return signerBuilder.build(privateKey);
            } catch (Exception e) {
                throw new RuntimeException(CONTENT_SIGNER_ERROR_MESSAGE, e);
            }
        }
}
