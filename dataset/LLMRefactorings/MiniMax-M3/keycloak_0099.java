public class keycloak_0099 {

        public static JOSE parse(String jwt) {
            String[] parts = jwt.split("\\.");
    
            if (parts.length == 0) {
                throw new RuntimeException("Could not infer header from JWT");
            }
    
            JsonNode header = parseHeader(parts[0]);
    
            if (header.has("enc")) {
                return new JWE(jwt);
            }
    
            try {
                return new JWSInput(jwt);
            } catch (JWSInputException cause) {
                throw new RuntimeException("Failed to build JWS", cause);
            }
        }
    
        private static JsonNode parseHeader(String headerPart) {
            try {
                return JsonSerialization.readValue(Base64Url.decode(headerPart), JsonNode.class);
            } catch (IOException cause) {
                throw new RuntimeException("Failed to parse JWT header", cause);
            }
        }
}
