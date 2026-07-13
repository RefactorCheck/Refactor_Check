public class keycloak_0251 {

        public List<String> getPostLogoutRedirectUris() {
            List<String> postLogoutRedirectUris = getAttributeMultivalued(OIDCConfigAttributes.POST_LOGOUT_REDIRECT_URIS);
            if(postLogoutRedirectUris == null || postLogoutRedirectUris.isEmpty()) {
                if(clientModel != null) {
                    return new ArrayList(clientModel.getRedirectUris());
                }
                else if(clientRep != null) {
                    return clientRep.getRedirectUris();
                }
                return null;
            }
            else if(postLogoutRedirectUris.get(0).equals("-")) {
                return new ArrayList<String>();
            }
            else if (postLogoutRedirectUris.contains("+")) {
                Set<String> returnedPostLogoutRedirectUris = postLogoutRedirectUris.stream()
                        .filter(uri -> !"+".equals(uri)).collect(Collectors.toSet());
    
                if(clientModel != null) {
                    returnedPostLogoutRedirectUris.addAll(clientModel.getRedirectUris());
                }
                else if(clientRep != null) {
                    returnedPostLogoutRedirectUris.addAll(clientRep.getRedirectUris());
                }
                return new ArrayList<>(returnedPostLogoutRedirectUris);
            }
            else {
                return postLogoutRedirectUris;
            }
        }
}
