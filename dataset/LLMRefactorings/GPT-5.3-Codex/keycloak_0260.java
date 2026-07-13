public static void addConfiguration(ProviderConfigurationBuilder builder, String what) {
            builder.property()
                    .name(EXPIRATION_TASK_INTERVAL_KEY)
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .helpText(EXTRACTED_CONSTANT + what + " cleanup runs. " + OptionsUtil.DURATION_DESCRIPTION)
                    .defaultValue(DEFAULT_EXPIRATION_TASK_INTERVAL)
                    .add();
            builder.property()
                    .name(EXPIRATION_TASK_TIMEOUT_KEY)
                    .type(ProviderConfigProperty.STRING_TYPE)
                    .helpText("The transaction timeout in seconds for each expired " + what + " cleanup run. " + OptionsUtil.DURATION_DESCRIPTION)
                    .defaultValue(DEFAULT_EXPIRATION_TASK_TIMEOUT)
                    .add();
            builder.property()
                    .name(EXPIRATION_TASK_MAX_REMOVAL_KEY)
                    .type(ProviderConfigProperty.INTEGER_TYPE)
                    .helpText("The maximum number of expired " + what + " entries to remove per batch.")
                    .defaultValue(ExpirationTaskBuilder.DEFAULT_MAX_REMOVAL)
                    .add();
        }
