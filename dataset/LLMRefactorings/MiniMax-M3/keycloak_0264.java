public class keycloak_0264 {

    private static final String REALMS_PATH = "realms";
    private static final String LOCATION_HEADER = "Location";
    private static final String ADMIN_ACCESS_LOG_FORMAT = "authenticated admin access for: %s";

    @Path(REALMS_PATH)
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
                logger.debugf(ADMIN_ACCESS_LOG_FORMAT, auth.getUser().getUsername());
            }
        }

        Cors.builder().checkAllowedOrigins(auth.getToken()).allowedMethods("GET", "PUT", "POST", "DELETE").exposedHeaders(LOCATION_HEADER).auth().add();

        return new RealmsAdminResource(session, auth, tokenManager);
    }
}
