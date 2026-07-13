public class kafka_0106 {

        public static MetricName getMetricName(
            String group,
            String typeName,
            String name,
            LinkedHashMap<String, String> tags
        ) {
            StringBuilder nameBuilder = new StringBuilder();
            nameBuilder.append(group);
            nameBuilder.append(":type=");
            nameBuilder.append(typeName);

            if (!name.isEmpty()) {
                nameBuilder.append(",name=");
                nameBuilder.append(name);
            }

            String scope = toScope(tags).orElse(null);
            Optional<String> tagsName = toMBeanName(tags);
            tagsName.ifPresent(nameBuilder::append);
            String metricName = nameBuilder.toString();

            return new MetricName(group, typeName, name, scope, metricName);
        }
}
