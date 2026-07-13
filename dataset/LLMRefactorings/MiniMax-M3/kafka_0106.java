public class kafka_0106 {

        public static MetricName getMetricName(
            String group,
            String typeName,
            String name,
            LinkedHashMap<String, String> tags
        ) {
            String scope = toScope(tags).orElse(null);
            return new MetricName(group, typeName, name, scope, buildMBeanName(group, typeName, name, tags));
        }

        private static String buildMBeanName(
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

            toMBeanName(tags).ifPresent(nameBuilder::append);
            return nameBuilder.toString();
        }
}
