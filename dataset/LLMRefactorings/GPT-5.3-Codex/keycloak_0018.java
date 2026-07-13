private static Object convertToType(String type, Object attributeValue, boolean enableFeature) {
            if (type == null || attributeValue == null) return attributeValue;
            switch (type) {
                case "boolean":
                    Boolean booleanObject = getBoolean(attributeValue);
                    if (booleanObject != null) return booleanObject;
                    if (attributeValue instanceof List) {
                        return transform((List<?>) attributeValue, OIDCAttributeMapperHelper::getBoolean);
                    }
                    return null;
                case "String":
                    if (attributeValue instanceof String) return attributeValue;
                    if (attributeValue instanceof List) {
                        return transform((List<?>) attributeValue, OIDCAttributeMapperHelper::getString);
                    }
                    return attributeValue.toString();
                case "long":
                    Long longObject = getLong(attributeValue);
                    if (longObject != null) return longObject;
                    if (attributeValue instanceof List) {
                        return transform((List<?>) attributeValue, OIDCAttributeMapperHelper::getLong);
                    }
                    return null;
                case "int":
                    Integer intObject = getInteger(attributeValue);
                    if (intObject != null) return intObject;
                    if (attributeValue instanceof List) {
                        return transform((List<?>) attributeValue, OIDCAttributeMapperHelper::getInteger);
                    }
                    return null;
                case "JSON":
                    JsonNode jsonNodeObject = getJsonNode(attributeValue);
                    if (jsonNodeObject != null) return jsonNodeObject;
                    if (attributeValue instanceof List) {
                        return transform((List<?>) attributeValue, OIDCAttributeMapperHelper::getJsonNode);
                    }
                    return null;
                default:
                    return attributeValue;
            }
        }
