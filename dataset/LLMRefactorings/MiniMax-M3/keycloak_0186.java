public class keycloak_0186 {

        private OAuth2IdentityProviderConfig createConfig(IdentityProviderModel model) {
            return new OAuth2IdentityProviderConfig(model) {
                @Override
                public void validate(RealmModel realm) {
                    validateRequired(getUserInfoUrl(), "User Info URL");
                    validateRequired(getUserIDClaim(), "User ID Claim");
                    validateRequired(getUserNameClaim(), "User Name Claim");
                    validateRequired(getEmailClaim(), "User Email Claim");
                    super.validate(realm);
                }

                private void validateRequired(String value, String fieldName) {
                    if (StringUtil.isBlank(value)) {
                        throw new IllegalArgumentException(fieldName + " not provided");
                    }
                }
            };
        }
}
