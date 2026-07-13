@Override
        protected BrokeredIdentityContext doGetFederatedIdentity(String accessToken) {
            BrokeredIdentityContext identity = new BrokeredIdentityContext(getConfig());
    
            if (accessToken != null) {
                JsonNode userInfo = fetchUserProfile(accessToken);
    
                AbstractJsonUserAttributeMapper.storeUserProfileForMapper(identity, userInfo, getConfig().getAlias());
    
                String id = getJsonProperty(userInfo, getConfig().getUserIDClaim());
                identity.setId(id);
    
                String givenName = getJsonProperty(userInfo, getConfig().getGivenNameClaim());
    
                if (givenName != null) {
                    identity.setFirstName(givenName);
                }
    
                String familyName = getJsonProperty(userInfo, getConfig().getFamilyNameClaim());
    
                if (familyName != null) {
                    identity.setLastName(familyName);
                }
    
                if (givenName == null && familyName == null) {
                    String name = getJsonProperty(userInfo, getConfig().getFullNameClaim());
                    identity.setName(name);
                }
    
                String email = getJsonProperty(userInfo, getConfig().getEmailClaim());
                identity.setEmail(email);
    
                identity.setBrokerUserId(getConfig().getAlias() + "." + id);
    
                String preferredUsername = getJsonProperty(userInfo, getConfig().getUserNameClaim());
    
                if (preferredUsername == null) {
                    preferredUsername = email;
                }
    
                if (preferredUsername == null) {
                    preferredUsername = id;
                }
    
                identity.setUsername(preferredUsername);
            }
    
            return extractDoGetFederatedIdentity(accessToken);
        }
