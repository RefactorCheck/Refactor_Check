public class nacos_0126 {

        @Override
        public AuthResult validateAuthority(IdentityContext identityContext, Permission permission) {
            try {
                initializeIfNeeded();
                
                OidcUser user = getAuthenticatedUser(identityContext);
                if (user == null) {
                    return createFailureResult("User not authenticated");
                }
                
                return checkAuthorization(user, permission);
                
            } catch (Exception e) {
                LOGGER.error("OIDC authorization error", e);
                return createFailureResult("Authorization failed");
            }
        }
        
        private OidcUser getAuthenticatedUser(IdentityContext identityContext) {
            OidcUser user = authManager.getUserFromContext(identityContext);
            if (user == null) {
                LOGGER.warn("No OIDC user found in context for authorization");
            }
            return user;
        }
        
        private AuthResult checkAuthorization(OidcUser user, Permission permission) {
            if (authManager.isGlobalAdmin(user)) {
                LOGGER.debug("User {} is global admin, authorization granted", user.getUsername());
                return AuthResult.successResult(user);
            }
            
            if (authManager.hasPermission(user, permission)) {
                LOGGER.debug("User {} authorized for {}:{}", user.getUsername(),
                    permission.getResource().getName(), permission.getAction());
                return AuthResult.successResult(user);
            }
            
            LOGGER.warn("User {} denied access to {}:{}", user.getUsername(),
                permission.getResource().getName(), permission.getAction());
            return createFailureResult("Access denied");
        }
        
        private AuthResult createFailureResult(String message) {
            return AuthResult.failureResult(HttpStatus.FORBIDDEN.value(), message);
        }
}
