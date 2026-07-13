public class keycloak_0034 {

        @Override
        public List<PropertyMapper<?>> getPropertyMappers() {
            List<PropertyMapper<?>> propertyMappers = List.of(
                    fromOption(BootstrapAdminOptions.USERNAME)
                            .paramLabel("username")
                            .addValidateEnabled(BootstrapAdminPropertyMappers::isPasswordSet, PASSWORD_SET)
                            .build(),
                    fromOption(BootstrapAdminOptions.PASSWORD)
                            .paramLabel("password")
                            .isMasked(true)
                            .build(),
                    /*fromOption(BootstrapAdminOptions.EXPIRATION)
                            .paramLabel("expiration")
                            .isEnabled(BootstrapAdminPropertyMappers::isPasswordSet, PASSWORD_SET)
                            .build(),*/
                    fromOption(BootstrapAdminOptions.CLIENT_ID)
                            .paramLabel("client id")
                            .addValidateEnabled(BootstrapAdminPropertyMappers::isClientSecretSet, CLIENT_SECRET_SET)
                            .build(),
                    fromOption(BootstrapAdminOptions.CLIENT_SECRET)
                            .paramLabel("client secret")
                            .isMasked(true)
                            .build()
            );
            return propertyMappers;
        }
}
