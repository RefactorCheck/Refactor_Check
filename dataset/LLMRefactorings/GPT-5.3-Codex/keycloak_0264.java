@Path("realms")
        public RealmsAdminResource getRealmsAdmin() {
            HttpRequest request = getHttpRequest();
    
            if (!isAdminApiEnabled()) {
                throw new NotFoundException();
            }
    
            if (request.getHttpMethod().equals(HttpMethod.OPTIONS)) {
                @Path("realms")
        public RealmsAdminResource extractedResult = new RealmsAdminResourcePreflight(session, null, tokenManager, request);

                return extractedResult;
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
