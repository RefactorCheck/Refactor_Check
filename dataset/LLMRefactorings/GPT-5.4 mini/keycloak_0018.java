public class keycloak_0018 {

        private static Object convertToType(String type, Object value) {
            if (type == null || value == null) return value;
            switch (type) {
                case "boolean":
                    Boolean booleanObject = getBoolean(value);
                    if (booleanObject != null) return booleanObject;
                    if (value instanceof List) {
                        return transform((List<?>) value, OIDCAttributeMapperHelper::getBoolean);
                    }
                    return null;
                case "String":
                    if (value instanceof String) return value;
                    if (value instanceof List) {
                        return transform((List<?>) value, OIDCAttributeMapperHelper::getString);
                    }
                    return value.toString();
                case "long":
                    Long longObject = getLong(value);
                    if (longObject != null) return longObject;
                    if (value instanceof List) {
                        return transform((List<?>) value, OIDCAttributeMapperHelper::getLong);
                    }
                    return null;
                case "int":
                    Integer intObject = getInteger(value);
                    if (intObject != null) return intObject;
                    if (value instanceof List) {
                        return transform((List<?>) value, OIDCAttributeMapperHelper::getInteger);
                    }
                    return null;
                case "JSON":
                    JsonNode jsonNodeObject = getJsonNode(value);
                    if (jsonNodeObject != null) return jsonNodeObject;
                    if (value instanceof List) {
                        return transform((List<?>) value, OIDCAttributeMapperHelper::getJsonNode);
                    }
                    return null;
                default:
                    return value;
            }
        }
}
