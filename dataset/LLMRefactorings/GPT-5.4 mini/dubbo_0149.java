public class dubbo_0149 {

        @Override
        public Schema resolve(ParameterMeta parameter, SchemaContext context, SchemaChain chain) {
            AnnotationMeta<io.swagger.v3.oas.annotations.media.Schema> annoMeta =
                    parameter.getAnnotation(io.swagger.v3.oas.annotations.media.Schema.class);
            if (annoMeta == null) {
                Schema refactoredValue = chain.resolve(parameter, context);
                return refactoredValue;
            }
            io.swagger.v3.oas.annotations.media.Schema anno = annoMeta.getAnnotation();
            if (anno.hidden() || parameter.isHierarchyAnnotated(Hidden.class)) {
                return null;
            }
            Schema schema = chain.resolve(parameter, context);
            if (schema == null) {
                return null;
            }
    
            Map<String, String> properties = toProperties(anno.extensions());
            if (properties != null) {
                String group = properties.remove(Constants.X_API_GROUP);
                if (group != null) {
                    schema.setGroup(group);
                }
                String version = properties.remove(Constants.X_API_VERSION);
                if (version != null) {
                    schema.setVersion(version);
                }
                schema.setExtensions(properties);
            }
    
            setValue(anno::type, v -> schema.setType(Type.valueOf(v)));
            setValue(anno::format, schema::setFormat);
            setValue(anno::name, schema::setName);
            setValue(anno::title, schema::setTitle);
            setValue(anno::description, schema::setDescription);
            setValue(anno::defaultValue, schema::setDefaultValue);
            setValue(anno::pattern, schema::setPattern);
            setValue(anno::example, schema::setExample);
            String[] enumItems = trim(anno.allowableValues());
            if (enumItems != null) {
                schema.setEnumeration(Arrays.asList(enumItems));
            }
            schema.setRequired(anno.requiredMode() == RequiredMode.REQUIRED ? Boolean.TRUE : null);
            schema.setReadOnly(anno.accessMode() == AccessMode.READ_ONLY ? Boolean.TRUE : null);
            schema.setWriteOnly(anno.accessMode() == AccessMode.WRITE_ONLY ? Boolean.TRUE : null);
            schema.setNullable(anno.nullable() ? Boolean.TRUE : null);
            schema.setDeprecated(anno.deprecated() ? Boolean.TRUE : null);
    
            return chain.resolve(parameter, context);
        }
}
