public class kafka_0106 {

        public static MetricName getMetricName(
            String group,
            String typeName,
            String name,
            LinkedHashMap<String, String> tags
        ) {
            final String DEFAULT_STRING_VALUE = ":type=";
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(group);
            nameBuilder.append(DEFAULT_STRING_VALUE);
            nameBuilder.append(typeName);
    
            if (!name.isEmpty()) {
                nameBuilder.append(",name=");
                nameBuilder.append(name);
            }
    
            String scope = toScope(tags).orElse(null);
            Optional<String> tagsName = toMBeanName(tags);
            tagsName.ifPresent(nameBuilder::append);
    
            return new MetricName(group, typeName, name, scope, nameBuilder.toString());
        }
}
