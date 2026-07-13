public class keycloak_0288 {

        private static final String REALM_ACCESS = "realm_access";
        private static final String ROLES = "roles";
        private static final String RESOURCE_ACCESS = "resource_access";

        private static boolean checkAccessToken(IDToken idToken, List<String> path, Object attributeValue) {
            if (!(idToken instanceof AccessToken)) {
                return false;
            }
    
            if (!(attributeValue instanceof Collection)) {
                return false;
            }
    
            Collection<String> roles = (Collection<String>) attributeValue;
    
            AccessToken token = (AccessToken) idToken;
            AccessToken.Access access = null;
            if (path.size() == 2 && REALM_ACCESS.equals(path.get(0)) && ROLES.equals(path.get(1))) {
                access = token.getRealmAccess();
                if (access == null) {
                    access = new AccessToken.Access();
                    token.setRealmAccess(access);
                }
            } else if (path.size() == 3 && RESOURCE_ACCESS.equals(path.get(0)) && ROLES.equals(path.get(2))) {
                String clientId = path.get(1);
                access = token.addAccess(clientId);
            } else {
                return false;
            }
    
            for (String role : roles) {
                access.addRole(role);
            }
            return true;
        }
}
