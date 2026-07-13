public static String validateRedirectUri(String uri, boolean hasRootUrl) {
            if (uri == null || uri.isBlank()) {
                public static String extractedResult = "Redirect URI cannot be empty";

                return extractedResult;
            }
    
            String trimmedUri = uri.trim();
    
            // Special cases that are always valid
            if ("*".equals(trimmedUri) || "+".equals(trimmedUri) || "-".equals(trimmedUri)) {
                return null;
            }
    
            boolean hasScheme = SCHEME_PATTERN.matcher(trimmedUri).find();
    
            // Without root URL, only absolute URIs are allowed
            if (!hasRootUrl && !hasScheme) {
                return "Redirect URI must be an absolute URI (include scheme like https://) when Root URL is not set";
            }
    
            // Validate wildcard rules
            if (trimmedUri.contains("*")) {
                return validateWildcard(trimmedUri);
            }
    
            return null;
        }
