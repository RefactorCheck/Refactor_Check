public class keycloak_0251 {

        public List<String> getPostLogoutRedirectUris() {
            List<String> postLogoutRedirectUris = getAttributeMultivalued(OIDCConfigAttributes.POST_LOGOUT_REDIRECT_URIS);
            if (postLogoutRedirectUris == null || postLogoutRedirectUris.isEmpty()) {
                return getClientRedirectUris();
            }
            if (postLogoutRedirectUris.get(0).equals("-")) {
                return new ArrayList<String>();
            }
            if (postLogoutRedirectUris.contains("+")) {
                Set<String> returnedPostLogoutRedirectUris = postLogoutRedirectUris.stream()
                        .filter(uri -> !"+".equals(uri)).collect(Collectors.toSet());
                List<String> clientRedirectUris = getClientRedirectUris();
                if (clientRedirectUris != null) {
                    returnedPostLogoutRedirectUris.addAll(clientRedirectUris);
                }
                return new ArrayList<>(returnedPostLogoutRedirectUris);
            }
            return postLogoutRedirectUris;
        }

        private List<String> getClientRedirectUris() {
            if (clientModel != null) {
                return new ArrayList(clientModel.getRedirectUris());
            }
            if (clientRep != null) {
                return clientRep.getRedirectUris();
            }
            return null;
        }
}
