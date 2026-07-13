public class keycloak_0223 {

    private static final String SHA256_WITH_RSA_ENCRYPTION = "SHA256WithRSAEncryption";
    private static final String SHA256_WITH_ECDSA = "SHA256WithECDSA";

    private ContentSigner createSigner(PrivateKey privateKey) {
        try {
            JcaContentSignerBuilder signerBuilder;
            switch (privateKey.getAlgorithm()) {
                case "RSA": {
                    signerBuilder = new JcaContentSignerBuilder(SHA256_WITH_RSA_ENCRYPTION)
                            .setProvider(BouncyIntegration.PROVIDER);
                    break;
                }
                case "EC": {
                    signerBuilder = new JcaContentSignerBuilder(SHA256_WITH_ECDSA)
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
}
