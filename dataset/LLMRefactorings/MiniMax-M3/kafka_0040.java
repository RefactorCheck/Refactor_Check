public class kafka_0040 {

    private static Map<String, Object> queryAttributes(MBeanServerConnection conn,
                                                       Set<ObjectName> objectNames,
                                                       Optional<String[]> attributesInclude) throws Exception {
        Map<String, Object> result = new HashMap<>();
        for (ObjectName objectName : objectNames) {
            MBeanInfo beanInfo = conn.getMBeanInfo(objectName);
            AttributeList attributes = conn.getAttributes(objectName,
                    Arrays.stream(beanInfo.getAttributes()).map(MBeanFeatureInfo::getName).toArray(String[]::new));
            addAttributesToResult(attributes, objectName, attributesInclude, result);
        }
        return result;
    }

    private static void addAttributesToResult(AttributeList attributes, ObjectName objectName,
                                              Optional<String[]> attributesInclude, Map<String, Object> result) {
        for (Attribute attribute : attributes.asList()) {
            if (attributesInclude.isPresent()) {
                if (List.of(attributesInclude.get()).contains(attribute.getName())) {
                    result.put(String.format("%s:%s", objectName.toString(), attribute.getName()),
                            attribute.getValue());
                }
            } else {
                result.put(String.format("%s:%s", objectName.toString(), attribute.getName()),
                        attribute.getValue());
            }
        }
    }
}
