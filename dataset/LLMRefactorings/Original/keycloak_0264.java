public class keycloak_0264 {

        @Path("realms")
        public RealmsAdminResource getRealmsAdmin() {
            HttpRequest request = getHttpRequest();
    
            if (!isAdminApiEnabled()) {
                throw new NotFoundException();
            }
    
            if (request.getHttpMethod().equals(HttpMethod.OPTIONS)) {
                return new RealmsAdminResourcePreflight(session, null, tokenManager, request);
            }
    
            AdminAuth auth = authenticateRealmAdminRequest(session);
            if (auth != null) {
                if (logger.isDebugEnabled()) {
                    logger.debugf("authenticated admin access for: %s", auth.getUser().getUsername());
                }
            }
    
            Cors.builder().checkAllowedOrigins(auth.getToken()).allowedMethods("GET", "PUT", "POST", "DELETE").exposedHeaders("Location").auth().add();
    
            return new RealmsAdminResource(session, auth, tokenManager);
        }
}
