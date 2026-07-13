public class keycloak_0005 {

        @Override
        public void setupConfiguration(SecureRequestObjectExecutor.Configuration config) {
            Configuration currentConfiguration = config == null ? new Configuration() : config;
            configuration = currentConfiguration;

            if (config == null || config.isVerifyNbf() == null) {
                configuration.setVerifyNbf(Boolean.TRUE);
            }
            if (config == null || config.getAvailablePeriod() == null) {
                configuration.setAvailablePeriod(DEFAULT_AVAILABLE_PERIOD);
            }
            if (config == null || config.isEncryptionRequired() == null) {
                configuration.setEncryptionRequired(Boolean.FALSE);
            }
            if (config == null || config.getAllowedClockSkew() == null) {
                configuration.setAllowedClockSkew(DEFAULT_ALLOWED_CLOCK_SKEW);
            }
        }
}
