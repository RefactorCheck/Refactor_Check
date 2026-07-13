public class keycloak_0018 {

    private static Object convertToType(String type, Object attributeValue) {
        if (type == null || attributeValue == null) return attributeValue;
        switch (type) {
            case "boolean":
                return convertSingleOrList(attributeValue, OIDCAttributeMapperHelper::getBoolean);
            case "String":
                return convertString(attributeValue);
            case "long":
                return convertSingleOrList(attributeValue, OIDCAttributeMapperHelper::getLong);
            case "int":
                return convertSingleOrList(attributeValue, OIDCAttributeMapperHelper::getInteger);
            case "JSON":
                return convertSingleOrList(attributeValue, OIDCAttributeMapperHelper::getJsonNode);
            default:
                return attributeValue;
        }
    }

    private static Object convertSingleOrList(Object attributeValue, Function<Object, ?> converter) {
        Object result = converter.apply(attributeValue);
        if (result != null) return result;
        if (attributeValue instanceof List) {
            return transform((List<?>) attributeValue, converter);
        }
        return null;
    }

    private static Object convertString(Object attributeValue) {
        if (attributeValue instanceof String) return attributeValue;
        if (attributeValue instanceof List) {
            return transform((List<?>) attributeValue, OIDCAttributeMapperHelper::getString);
        }
        return attributeValue.toString();
    }
}
