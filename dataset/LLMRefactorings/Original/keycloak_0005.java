public class keycloak_0005 {

        @Override
        public void setupConfiguration(SecureRequestObjectExecutor.Configuration config) {
            if (config == null) {
                configuration = new Configuration();
                configuration.setVerifyNbf(Boolean.TRUE);
                configuration.setAvailablePeriod(DEFAULT_AVAILABLE_PERIOD);
                configuration.setEncryptionRequired(Boolean.FALSE);
                configuration.setAllowedClockSkew(DEFAULT_ALLOWED_CLOCK_SKEW);
            } else {
                configuration = config;
                if (config.isVerifyNbf() == null) {
                    configuration.setVerifyNbf(Boolean.TRUE);
                }
                if (config.getAvailablePeriod() == null) {
                    configuration.setAvailablePeriod(DEFAULT_AVAILABLE_PERIOD);
                }
                if (config.isEncryptionRequired() == null) {
                    configuration.setEncryptionRequired(Boolean.FALSE);
                }
                if (config.getAllowedClockSkew() == null) {
                    configuration.setAllowedClockSkew(DEFAULT_ALLOWED_CLOCK_SKEW);
                }
            }
        }
}
