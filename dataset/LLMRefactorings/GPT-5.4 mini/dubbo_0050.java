public static class dubbo_0050 {

        @Override
        public Operation resolve(Operation operation, MethodMeta methodMeta, OperationContext ctx, OperationChain chain) {
            AnnotationMeta<?> annoMeta = methodMeta.findAnnotation(Annotations.Operation);
            if (annoMeta == null) {
                return chain.resolve(operation, methodMeta, ctx);
            }
            if (annoMeta.getBoolean(HIDDEN)) {
                return null;
            }
    
            String[] tags = trim(annoMeta.getStringArray("tags"));
            if (tags != null) {
                operation.setTags(new LinkedHashSet<>(Arrays.asList(tags)));
            }
    
            String summary = trim(annoMeta.getValue());
            if (summary == null) {
                summary = trim(annoMeta.getString("summary"));
            }
            operation
                    .setGroup(trim(annoMeta.getString("group")))
                    .setVersion(trim(annoMeta.getString("version")))
                    .setOperationId(trim(annoMeta.getString("id")))
                    .setSummary(summary)
                    .setDescription(trim(annoMeta.getString("description")))
                    .setDeprecated(annoMeta.getBoolean("deprecated"))
                    .setExtensions(Helper.toProperties(annoMeta.getStringArray("extensions")));
    
            return chain.resolve(operation, methodMeta, ctx);
        }
}
