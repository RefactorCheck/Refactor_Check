public class keycloak_0254 {

    private static final String WILDCARD_URI = "*";
    private static final String INCREMENT_URI = "+";
    private static final String DECREMENT_URI = "-";
    private static final String EMPTY_URI_ERROR = "Redirect URI cannot be empty";
    private static final String ABSOLUTE_URI_ERROR = "Redirect URI must be an absolute URI (include scheme like https://) when Root URL is not set";

        public static String validateRedirectUri(String uri, boolean hasRootUrl) {
            if (uri == null || uri.isBlank()) {
                return EMPTY_URI_ERROR;
            }
    
            String trimmedUri = uri.trim();
    
            // Special cases that are always valid
            if (WILDCARD_URI.equals(trimmedUri) || INCREMENT_URI.equals(trimmedUri) || DECREMENT_URI.equals(trimmedUri)) {
                return null;
            }
    
            boolean hasScheme = SCHEME_PATTERN.matcher(trimmedUri).find();
    
            // Without root URL, only absolute URIs are allowed
            if (!hasRootUrl && !hasScheme) {
                return ABSOLUTE_URI_ERROR;
            }
    
            // Validate wildcard rules
            if (trimmedUri.contains(WILDCARD_URI)) {
                return validateWildcard(trimmedUri);
            }
    
            return null;
        }
}
