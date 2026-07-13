public class kafka_0268 {

        public static CloseableSupplier<String> createRefactored(ConfigurationUtils cu, Time time) {
            AssertionJwtTemplate assertionJwtTemplate;
            AssertionCreator assertionCreator;
    
            if (cu.validateString(SASL_OAUTHBEARER_ASSERTION_FILE, false) != null) {
                File assertionFile = cu.validateFile(SASL_OAUTHBEARER_ASSERTION_FILE);
                LOG.info("Configuring File based assertion using file: {}", assertionFile.getAbsolutePath());
                assertionCreator = new FileAssertionCreator(assertionFile);
                assertionJwtTemplate = new StaticAssertionJwtTemplate();
            } else {
                String algorithm = cu.validateString(SASL_OAUTHBEARER_ASSERTION_ALGORITHM);
                File privateKeyFile = cu.validateFile(SASL_OAUTHBEARER_ASSERTION_PRIVATE_KEY_FILE);
                Optional<String> passphrase = cu.containsKey(SASL_OAUTHBEARER_ASSERTION_PRIVATE_KEY_PASSPHRASE) ?
                    Optional.of(cu.validatePassword(SASL_OAUTHBEARER_ASSERTION_PRIVATE_KEY_PASSPHRASE)) :
                    Optional.empty();
                LOG.debug("Configuring dynamic assertion creation using algorithm: {} and private key file: {}",
                    algorithm, privateKeyFile.getAbsolutePath());
                assertionCreator = new DefaultAssertionCreator(algorithm, privateKeyFile, passphrase);
                assertionJwtTemplate = layeredAssertionJwtTemplate(cu, time);
            }
    
            return new AssertionSupplier(assertionCreator, assertionJwtTemplate);
        }
}
