public class dubbo_0198 {

        public static Map<String, Object> getSubProperties(
                PropertySources propertySources, PropertyResolver propertyResolver, String prefix) {
    
            Map<String, Object> subProperties = new LinkedHashMap<>();
    
            String normalizedPrefix = normalizePrefix(prefix);
    
            Iterator<PropertySource<?>> iterator = propertySources.iterator();
    
            while (iterator.hasNext()) {
                PropertySource<?> source = iterator.next();
                for (String name : getPropertyNames(source)) {
                    if (!subProperties.containsKey(name) && name.startsWith(normalizedPrefix)) {
                        String subName = name.substring(normalizedPrefix.length());
                        if (!subProperties.containsKey(subName)) { // take first one
                            Object value = source.getProperty(name);
                            if (value instanceof String) {
                                // Resolve placeholder
                                value = propertyResolver.resolvePlaceholders((String) value);
                            }
                            subProperties.put(subName, value);
                        }
                    }
                }
            }
    
            return unmodifiableMap(subProperties);
        }
}
