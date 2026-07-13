public class keycloak_0036 {

        private String buildTypeArgumentDescription(FieldInfo field) {
            Type fieldType = field.type();
            if (fieldType.kind() != Type.Kind.PARAMETERIZED_TYPE) {
                return null;
            }
    
            List<Type> typeArguments = fieldType.asParameterizedType().arguments();
            if (typeArguments.isEmpty()) {
                return null;
            }
    
            List<String> constraintDescriptions = new ArrayList<>();
    
            for (Type typeArgument : typeArguments) {
                for (AnnotationInstance annotation : typeArgument.annotations()) {
                    DotName name = annotation.name();
                    // @URL is always handled explicitly: it must appear in OpenAPI prose even if the
                    // Jandex index does not list the annotation type as @Constraint (thin classpath).
                    if (URL.equals(name)) {
                        String description = buildConstraintDescription(annotation);
                        if (description != null) {
                            constraintDescriptions.add(description);
                        }
                        continue;
                    }
                    if (!isConstraintAnnotation(name)) {
                        continue;
                    }
    
                    String description = buildConstraintDescription(annotation);
                    if (description != null) {
                        constraintDescriptions.add(description);
                    }
                }
            }
    
            if (constraintDescriptions.isEmpty()) {
                return null;
            }
    
            StringJoiner joiner = new StringJoiner(", ");
            constraintDescriptions.forEach(joiner::add);
            return "each element " + joiner;
        }
}
