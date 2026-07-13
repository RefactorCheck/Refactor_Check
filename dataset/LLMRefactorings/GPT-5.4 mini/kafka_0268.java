public class kafka_0268 {

        public static CloseableSupplier<String> create(ConfigurationUtils cu, Time time) {
            if (cu.validateString(SASL_OAUTHBEARER_ASSERTION_FILE, false) != null) {
                return createFileBasedAssertion(cu);
            }
            return createDynamicAssertion(cu, time);
        }

        private static CloseableSupplier<String> createFileBasedAssertion(ConfigurationUtils cu) {
            File assertionFile = cu.validateFile(SASL_OAUTHBEARER_ASSERTION_FILE);
            LOG.info("Configuring File based assertion using file: {}", assertionFile.getAbsolutePath());
            AssertionCreator assertionCreator = new FileAssertionCreator(assertionFile);
            AssertionJwtTemplate assertionJwtTemplate = new StaticAssertionJwtTemplate();
            return new AssertionSupplier(assertionCreator, assertionJwtTemplate);
        }

        private static CloseableSupplier<String> createDynamicAssertion(ConfigurationUtils cu, Time time) {
            String algorithm = cu.validateString(SASL_OAUTHBEARER_ASSERTION_ALGORITHM);
            File privateKeyFile = cu.validateFile(SASL_OAUTHBEARER_ASSERTION_PRIVATE_KEY_FILE);
            Optional<String> passphrase = cu.containsKey(SASL_OAUTHBEARER_ASSERTION_PRIVATE_KEY_PASSPHRASE) ?
                Optional.of(cu.validatePassword(SASL_OAUTHBEARER_ASSERTION_PRIVATE_KEY_PASSPHRASE)) :
                Optional.empty();
            LOG.debug("Configuring dynamic assertion creation using algorithm: {} and private key file: {}",
                algorithm, privateKeyFile.getAbsolutePath());
            AssertionCreator assertionCreator = new DefaultAssertionCreator(algorithm, privateKeyFile, passphrase);
            AssertionJwtTemplate assertionJwtTemplate = layeredAssertionJwtTemplate(cu, time);
            return new AssertionSupplier(assertionCreator, assertionJwtTemplate);
        }
}
