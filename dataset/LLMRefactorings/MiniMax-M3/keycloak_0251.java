public class keycloak_0251 {

    public List<String> getPostLogoutRedirectUris() {
        List<String> postLogoutRedirectUris = getAttributeMultivalued(OIDCConfigAttributes.POST_LOGOUT_REDIRECT_URIS);
        if(postLogoutRedirectUris == null || postLogoutRedirectUris.isEmpty()) {
            Collection<String> redirectUris = getRedirectUris();
            return redirectUris == null ? null : new ArrayList<>(redirectUris);
        }
        else if(postLogoutRedirectUris.get(0).equals("-")) {
            return new ArrayList<String>();
        }
        else if (postLogoutRedirectUris.contains("+")) {
            Set<String> returnedPostLogoutRedirectUris = postLogoutRedirectUris.stream()
                    .filter(uri -> !"+".equals(uri)).collect(Collectors.toSet());
            Collection<String> redirectUris = getRedirectUris();
            if (redirectUris != null) {
                returnedPostLogoutRedirectUris.addAll(redirectUris);
            }
            return new ArrayList<>(returnedPostLogoutRedirectUris);
        }
        else {
            return postLogoutRedirectUris;
        }
    }

    private Collection<String> getRedirectUris() {
        if(clientModel != null) {
            return clientModel.getRedirectUris();
        }
        else if(clientRep != null) {
            return clientRep.getRedirectUris();
        }
        return null;
    }
}
