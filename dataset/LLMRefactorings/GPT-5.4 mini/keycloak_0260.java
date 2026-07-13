public class keycloak_0260 {

        public static void addConfiguration(ProviderConfigurationBuilder builder, String what) {
            String expirationIntervalHelpText = "The interval in seconds between expired " + what + " cleanup runs. " + OptionsUtil.DURATION_DESCRIPTION;
            String expirationTimeoutHelpText = "The transaction timeout in seconds for each expired " + what + " cleanup run. " + OptionsUtil.DURATION_DESCRIPTION;
            String expirationMaxRemovalHelpText = "The maximum number of expired " + what + " entries to remove per batch.";
            builder.property()
                    .name(EXPIRATION_TASK_INTERVAL_KEY)
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .helpText(expirationIntervalHelpText)
                    .defaultValue(DEFAULT_EXPIRATION_TASK_INTERVAL)
                    .add();
            builder.property()
                    .name(EXPIRATION_TASK_TIMEOUT_KEY)
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .helpText(expirationTimeoutHelpText)
                    .defaultValue(DEFAULT_EXPIRATION_TASK_TIMEOUT)
                    .add();
            builder.property()
                    .name(EXPIRATION_TASK_MAX_REMOVAL_KEY)
                    .type(ProviderConfigProperty.INTEGER_TYPE)
                    .helpText(expirationMaxRemovalHelpText)
                    .defaultValue(ExpirationTaskBuilder.DEFAULT_MAX_REMOVAL)
                    .add();
        }
}
