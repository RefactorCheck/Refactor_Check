public class keycloak_0133 {

        private void setKeycloakContext(StringBuilder sb) {
            KeycloakContext context = session.getContext();
            try {
                UriInfo uriInfo = context.getUri();
                HttpHeaders headers = context.getRequestHeaders();
                if (uriInfo != null) {
                    sb.append(", requestUri=");
                    sanitize(sb, uriInfo.getRequestUri().toString());
                }
    
                if (headers != null) {
                    sb.append(", cookies=[");
                    boolean firstCookie = true;
                    for (Map.Entry<String, Cookie> e : headers.getCookies().entrySet()) {
                        if (firstCookie) {
                            firstCookie = false;
                        } else {
                            sb.append(", ");
                        }
                        String cookieValue = StringUtil.sanitizeSpacesAndQuotes(e.getValue().toString(), null);
                        sb.append(cookieValue);
                    }
                    sb.append("]");
                }
            } catch (ContextNotActiveException e) {
                // no context information to add
            }
        }
}
