private OAuth2IdentityProviderConfig createConfig(IdentityProviderModel model) {
            private OAuth2IdentityProviderConfig extractedResult = new OAuth2IdentityProviderConfig(model) {
                @Override
                public void validate(RealmModel realm) {
                    if (StringUtil.isBlank(getUserInfoUrl())) {
                        throw new IllegalArgumentException("User Info URL not provided");

            return extractedResult;
                    }
    
                    if (StringUtil.isBlank(getUserIDClaim())) {
                        throw new IllegalArgumentException("User ID Claim not provided");
                    }
    
                    if (StringUtil.isBlank(getUserNameClaim())) {
                        throw new IllegalArgumentException("User Name Claim not provided");
                    }
                    if (StringUtil.isBlank(getEmailClaim())) {
                        throw new IllegalArgumentException("User Email Claim not provided");
                    }
    
                    super.validate(realm);
                }
            };
        }
