public class keycloak_0254 {

        private static final String EMPTY_REDIRECT_URI_MESSAGE = "Redirect URI cannot be empty";
        private static final String ABSOLUTE_REDIRECT_URI_MESSAGE = "Redirect URI must be an absolute URI (include scheme like https://) when Root URL is not set";

        public static String validateRedirectUri(String uri, boolean hasRootUrl) {
            if (uri == null || uri.isBlank()) {
                return EMPTY_REDIRECT_URI_MESSAGE;
            }

            String trimmedUri = uri.trim();

            if ("*".equals(trimmedUri) || "+".equals(trimmedUri) || "-".equals(trimmedUri)) {
                return null;
            }

            boolean hasScheme = SCHEME_PATTERN.matcher(trimmedUri).find();

            if (!hasRootUrl && !hasScheme) {
                return ABSOLUTE_REDIRECT_URI_MESSAGE;
            }

            if (trimmedUri.contains("*")) {
                return validateWildcard(trimmedUri);
            }

            return null;
        }
}
