public class nacos_0126 {


        @Override
        public AuthResult validateAuthorityRefactored(IdentityContext identityContext, Permission permission) {
            try {
                initializeIfNeeded();
                
                // Get user from context
                OidcUser user = authManager.getUserFromContext(identityContext);
                if (user == null) {
                    LOGGER.warn("No OIDC user found in context for authorization");
                    return AuthResult.failureResult(HttpStatus.FORBIDDEN.value(),
                        "User not authenticated");
                }
                
                // Check if user is global admin
                if (authManager.isGlobalAdmin(user)) {
                    LOGGER.debug("User {} is global admin, authorization granted", user.getUsername());
                    return AuthResult.successResult(user);
                }
                
                // Check permission
                if (authManager.hasPermission(user, permission)) {
                    LOGGER.debug("User {} authorized for {}:{}", user.getUsername(),
                        permission.getResource().getName(), permission.getAction());
                    return AuthResult.successResult(user);
                }
                
                LOGGER.warn("User {} denied access to {}:{}", user.getUsername(),
                    permission.getResource().getName(), permission.getAction());
                return AuthResult.failureResult(HttpStatus.FORBIDDEN.value(), "Access denied");
                
            } catch (Exception e) {
                LOGGER.error("OIDC authorization error", e);
                return AuthResult.failureResult(HttpStatus.FORBIDDEN.value(), "Authorization failed");
            }
        
        }
}
