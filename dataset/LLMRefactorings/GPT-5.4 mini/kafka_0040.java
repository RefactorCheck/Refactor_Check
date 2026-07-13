public class kafka_0040 {

        private static Map<String, Object> queryAttributes(MBeanServerConnection conn,
                                                           Set<ObjectName> objectNames,
                                                           Optional<String[]> attributesInclude) throws Exception {
            Map<String, Object> result = new HashMap<>();
            for (ObjectName objectName : objectNames) {
                MBeanInfo beanInfo = conn.getMBeanInfo(objectName);
                AttributeList attributes = conn.getAttributes(objectName,
                        Arrays.stream(beanInfo.getAttributes()).map(MBeanFeatureInfo::getName).toArray(String[]::new));
                for (Attribute attribute : attributes.asList()) {
                    final String attributeKey = String.format("%s:%s", objectName.toString(), attribute.getName());
                    if (attributesInclude.isPresent()) {
                        if (List.of(attributesInclude.get()).contains(attribute.getName())) {
                            result.put(attributeKey, attribute.getValue());
                        }
                    } else {
                        result.put(attributeKey, attribute.getValue());
                    }
                }
            }
            return result;
        }
}
