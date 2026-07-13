public class nacos_0144 {

        public static AuthorizationResponse fromJson(String json) {
            if (json == null || json.trim().isEmpty()) {
                return denied("Empty response from IdP");
            }
            
            AuthorizationResponse response = new AuthorizationResponse();
            
            // Check for "allowed" field
            if (json.contains(OidcConstants.JSON_FIELD_ALLOWED)) {
                response.allowed = json.contains("\"allowed\":true")
                    || json.contains("\"allowed\": true");
            } else if (json.contains(OidcConstants.JSON_FIELD_RESULT)) {
                // Keycloak format
                response.allowed = json.toLowerCase().contains("\"result\":\"permit\"")
                    || json.toLowerCase().contains("\"result\": \"permit\"");
            } else if (json.contains(OidcConstants.JSON_FIELD_DECISION)) {
                // Alternative format
                response.allowed = json.toLowerCase().contains("\"decision\":\"permit\"")
                    || json.toLowerCase().contains("\"decision\": \"permit\"");
            }
            
            response.reason = extractFirstAvailable(json, "reason", "message", "error_description");
            response.errorCode = extractFirstAvailable(json, "error", "errorCode");
            
            return response;
        }
        
        private static String extractFirstAvailable(String json, String... fieldNames) {
            for (String fieldName : fieldNames) {
                String value = extractJsonValue(json, fieldName);
                if (value != null) {
                    return value;
                }
            }
            return null;
        }
}
