public class keycloak_0195 {

        private UserModel resolveUser(ResourcePermission permission, AuthorizationProvider authorization) {
            RealmModel realm = authorization.getRealm();
            KeycloakSession session = authorization.getKeycloakSession();
            String resourceType = permission.getResourceType();
    
            if (resourceType == null) {
                return null;
            }
    
            Resource resource = permission.getResource();
    
            if (resource == null) {
                return null;
            }
    
            ResourceServer resourceServer = resource.getResourceServer();
            String userName = AdminPermissionsSchema.SCHEMA.getResourceName(session, resourceServer, resourceType, resource.getName());
    
            if (userName == null) {
                return null;
            }
    
            return session.users().getUserByUsername(realm, userName);
        }
}
