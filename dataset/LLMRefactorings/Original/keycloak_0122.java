public class keycloak_0122 {

        public static String getRedirectTo(HttpFacade facade, String contextPath, String baseUri) {
            String redirectTo = facade.getRequest().getQueryParamValue("redirectTo");
            if (redirectTo != null && !redirectTo.isEmpty()) {
                return buildRedirectTo(baseUri, redirectTo);
            } else {
                redirectTo = facade.getRequest().getFirstParam(GeneralConstants.RELAY_STATE);
                if (redirectTo != null) {
                    int index = redirectTo.indexOf("redirectTo=");
                    if (index >= 0) {
                        String to = redirectTo.substring(index + "redirectTo=".length());
                        index = to.indexOf(';');
                        if (index >=0) {
                            to = to.substring(0, index);
                        }
                        return buildRedirectTo(baseUri, to);
                    }
                }
                if (contextPath.isEmpty()) baseUri += "/";
                return baseUri;
            }
        }
}
