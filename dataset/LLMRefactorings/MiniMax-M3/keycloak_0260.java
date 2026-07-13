public class keycloak_0260 {

        public static void addConfiguration(ProviderConfigurationBuilder builder, String what) {
            addProperty(builder, EXPIRATION_TASK_INTERVAL_KEY, ProviderConfigProperty.STRING_TYPE,
                    "The interval in seconds between expired " + what + " cleanup runs. " + OptionsUtil.DURATION_DESCRIPTION,
                    DEFAULT_EXPIRATION_TASK_INTERVAL);
            addProperty(builder, EXPIRATION_TASK_TIMEOUT_KEY, ProviderConfigProperty.STRING_TYPE,
                    "The transaction timeout in seconds for each expired " + what + " cleanup run. " + OptionsUtil.DURATION_DESCRIPTION,
                    DEFAULT_EXPIRATION_TASK_TIMEOUT);
            addProperty(builder, EXPIRATION_TASK_MAX_REMOVAL_KEY, ProviderConfigProperty.INTEGER_TYPE,
                    "The maximum number of expired " + what + " entries to remove per batch.",
                    ExpirationTaskBuilder.DEFAULT_MAX_REMOVAL);
        }

        private static void addProperty(ProviderConfigurationBuilder builder, String name, String type,
                String helpText, Object defaultValue) {
            builder.property()
                    .name(name)
                    .type(type)
                    .helpText(helpText)
                    .defaultValue(defaultValue)
                    .add();
        }
}
