public class keycloak_0070 {

        private UserModel resolveUser(BackchannelAuthenticationEndpointRequest endpointRequest, String authRequestedUserHint) {
            CIBALoginUserResolver resolver = session.getProvider(CIBALoginUserResolver.class);
    
            if (resolver == null) {
                throw new RuntimeException("CIBA Login User Resolver not setup properly.");
            }
    
            String userHint;
            UserModel user;
    
            if (authRequestedUserHint.equals(LOGIN_HINT_PARAM)) {
                userHint = endpointRequest.getLoginHint();
                if (userHint == null)
                    throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "missing parameter : login_hint",
                            Response.Status.BAD_REQUEST);
                user = resolver.getUserFromLoginHint(userHint);
            } else if (authRequestedUserHint.equals(ID_TOKEN_HINT)) {
                userHint = endpointRequest.getIdTokenHint();
                if (userHint == null)
                    throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "missing parameter : id_token_hint",
                            Response.Status.BAD_REQUEST);
                user = resolver.getUserFromIdTokenHint(userHint);
            } else if (authRequestedUserHint.equals(CibaGrantType.LOGIN_HINT_TOKEN)) {
                userHint = endpointRequest.getLoginHintToken();
                if (userHint == null)
                    throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "missing parameter : login_hint_token",
                            Response.Status.BAD_REQUEST);
                user = resolver.getUserFromLoginHintToken(userHint);
            } else {
                throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST,
                        "invalid user hint", Response.Status.BAD_REQUEST);
            }
    
    
            BruteForceProtector protector = session.getProvider(BruteForceProtector.class);
            boolean isInvalidUser = (user == null || !user.isEnabled());
            if (!isInvalidUser && AuthenticatorUtils.getDisabledByBruteForceEventError(protector, session, realm, user) != null) {
                isInvalidUser = true;
            }
    
            if (isInvalidUser) {
                throw new ErrorResponseException(OAuthErrorException.INVALID_REQUEST, "invalid_user", Response.Status.BAD_REQUEST);
            }
    
            return user;
        }
}
