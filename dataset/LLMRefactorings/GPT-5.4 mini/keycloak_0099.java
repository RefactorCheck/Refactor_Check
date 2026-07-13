public class keycloak_0099 {

        public static JOSE parse(String jwt) {
            String[] jwtParts = jwt.split("\\.");
    
            if (jwtParts.length == 0) {
                throw new RuntimeException("Could not infer header from JWT");
            }
    
            JsonNode jwtHeader;
    
            try {
                jwtHeader = JsonSerialization.readValue(Base64Url.decode(jwtParts[0]), JsonNode.class);
            } catch (IOException cause) {
                throw new RuntimeException("Failed to parse JWT header", cause);
            }
    
            if (jwtHeader.has("enc")) {
                return new JWE(jwt);
            }
    
            try {
                return new JWSInput(jwt);
            } catch (JWSInputException cause) {
                throw new RuntimeException("Failed to build JWS", cause);
            }
        }
}
