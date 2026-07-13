public class keycloak_0288 {

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
            boolean realmRolesPath = path.size() == 2 && "realm_access".equals(path.get(0)) && "roles".equals(path.get(1));
            boolean clientRolesPath = path.size() == 3 && "resource_access".equals(path.get(0)) && "roles".equals(path.get(2));
            if (realmRolesPath) {
                access = token.getRealmAccess();
                if (access == null) {
                    access = new AccessToken.Access();
                    token.setRealmAccess(access);
                }
            } else if (clientRolesPath) {
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
