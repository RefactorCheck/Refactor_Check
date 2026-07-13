public class nacos_0144 {


        public static AuthorizationResponse fromJsonRefactored(String json) {
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
            
            // Extract reason if present
            response.reason = extractJsonValue(json, "reason");
            if (response.reason == null) {
                response.reason = extractJsonValue(json, "message");
            }
            if (response.reason == null) {
                response.reason = extractJsonValue(json, "error_description");
            }
            
            // Extract error code if present
            response.errorCode = extractJsonValue(json, "error");
            if (response.errorCode == null) {
                response.errorCode = extractJsonValue(json, "errorCode");
            }
            
            return response;
        
        }
}
