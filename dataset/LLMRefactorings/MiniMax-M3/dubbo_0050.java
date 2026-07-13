public class dubbo_0050 {

        private static final String TAGS = "tags";
        private static final String GROUP = "group";
        private static final String VERSION = "version";
        private static final String ID = "id";
        private static final String SUMMARY = "summary";
        private static final String DESCRIPTION = "description";
        private static final String DEPRECATED = "deprecated";
        private static final String EXTENSIONS = "extensions";

        @Override
        public Operation resolve(Operation operation, MethodMeta methodMeta, OperationContext ctx, OperationChain chain) {
            AnnotationMeta<?> annoMeta = methodMeta.findAnnotation(Annotations.Operation);
            if (annoMeta == null) {
                return chain.resolve(operation, methodMeta, ctx);
            }
            if (annoMeta.getBoolean(HIDDEN)) {
                return null;
            }

            String[] tags = trim(annoMeta.getStringArray(TAGS));
            if (tags != null) {
                operation.setTags(new LinkedHashSet<>(Arrays.asList(tags)));
            }

            String summary = trim(annoMeta.getValue());
            if (summary == null) {
                summary = trim(annoMeta.getString(SUMMARY));
            }
            operation
                    .setGroup(trim(annoMeta.getString(GROUP)))
                    .setVersion(trim(annoMeta.getString(VERSION)))
                    .setOperationId(trim(annoMeta.getString(ID)))
                    .setSummary(summary)
                    .setDescription(trim(annoMeta.getString(DESCRIPTION)))
                    .setDeprecated(annoMeta.getBoolean(DEPRECATED))
                    .setExtensions(Helper.toProperties(annoMeta.getStringArray(EXTENSIONS)));

            return chain.resolve(operation, methodMeta, ctx);
        }
}
