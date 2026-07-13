public class dubbo_0031 {

        public static Map<String, Object> getAttributes(
                Map<String, Object> annotationAttributes,
                PropertyResolver propertyResolver,
                String... ignoreAttributeNames) {
    
            Set<String> ignoreAttributeNamesSet =
                    new HashSet<String>((Collection<? extends String>) arrayToList(ignoreAttributeNames));
    
            Map<String, Object> actualAttributes = new LinkedHashMap<String, Object>();
    
            for (Map.Entry<String, Object> annotationAttribute : annotationAttributes.entrySet()) {
    
                String attributeName = annotationAttribute.getKey();
                Object attributeValue = annotationAttribute.getValue();
    
                // ignore attribute name
                if (ignoreAttributeNamesSet.contains(attributeName)) {
                    continue;
                }
    
                actualAttributes.put(attributeName, resolveAttributeValue(attributeValue, propertyResolver));
            }
            return actualAttributes;
        }

        private static Object resolveAttributeValue(Object attributeValue, PropertyResolver propertyResolver) {
            if (attributeValue instanceof String) {
                return resolvePlaceholders(valueOf(attributeValue), propertyResolver);
            } else if (attributeValue instanceof String[]) {
                String[] values = (String[]) attributeValue;
                for (int i = 0; i < values.length; i++) {
                    values[i] = resolvePlaceholders(values[i], propertyResolver);
                }
                return values;
            }
            return attributeValue;
        }
}
