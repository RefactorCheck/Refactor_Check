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
                appendCookies(sb, headers);
            }
        } catch (ContextNotActiveException e) {
            // no context information to add
        }
    }

    private void appendCookies(StringBuilder sb, HttpHeaders headers) {
        sb.append(", cookies=[");
        boolean f = true;
        for (Map.Entry<String, Cookie> e : headers.getCookies().entrySet()) {
            if (f) {
                f = false;
            } else {
                sb.append(", ");
            }
            sb.append(StringUtil.sanitizeSpacesAndQuotes(e.getValue().toString(), null));
        }
        sb.append("]");
    }
}
