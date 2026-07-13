public class kafka_0179 {

        @Override
        public void initialize(Subject subject, CallbackHandler callbackHandler, Map<String, ?> sharedState, Map<String, ?> options) {
            this.callbackHandler = callbackHandler;
            fileName = (String) options.get(FILE_OPTIONS);
            if (Utils.isBlank(fileName)) {
                throw new ConfigException("Property Credentials file must be specified");
            }

            final boolean credentialsCached = CREDENTIAL_PROPERTIES.containsKey(fileName);
            if (!credentialsCached) {
                log.trace("Opening credential properties file '{}'", fileName);
                Properties credentialProperties = new Properties();
                try {
                    try (InputStream inputStream = Files.newInputStream(Paths.get(fileName))) {
                        log.trace("Parsing credential properties file '{}'", fileName);
                        credentialProperties.load(inputStream);
                    }
                    CREDENTIAL_PROPERTIES.putIfAbsent(fileName, credentialProperties);
                    if (credentialProperties.isEmpty())
                        log.warn("Credential properties file '{}' is empty; all requests will be permitted",
                            fileName);
                } catch (IOException e) {
                    log.error("Error loading credentials file ", e);
                    throw new ConfigException("Error loading Property Credentials file");
                }
            } else {
                log.trace(
                    "Credential properties file '{}' has already been opened and parsed; will read from cached, in-memory store",
                    fileName);
            }
        }
}
