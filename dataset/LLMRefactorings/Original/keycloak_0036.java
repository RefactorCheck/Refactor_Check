public class keycloak_0036 {

        private String buildTypeArgumentDescription(FieldInfo field) {
            Type fieldType = field.type();
            if (fieldType.kind() != Type.Kind.PARAMETERIZED_TYPE) {
                return null;
            }
    
            List<Type> typeArgs = fieldType.asParameterizedType().arguments();
            if (typeArgs.isEmpty()) {
                return null;
            }
    
            List<String> elementConstraints = new ArrayList<>();
    
            for (Type typeArg : typeArgs) {
                for (AnnotationInstance annotation : typeArg.annotations()) {
                    DotName name = annotation.name();
                    // @URL is always handled explicitly: it must appear in OpenAPI prose even if the
                    // Jandex index does not list the annotation type as @Constraint (thin classpath).
                    if (URL.equals(name)) {
                        String constraintDesc = buildConstraintDescription(annotation);
                        if (constraintDesc != null) {
                            elementConstraints.add(constraintDesc);
                        }
                        continue;
                    }
                    if (!isConstraintAnnotation(name)) {
                        continue;
                    }
    
                    String constraintDesc = buildConstraintDescription(annotation);
                    if (constraintDesc != null) {
                        elementConstraints.add(constraintDesc);
                    }
                }
            }
    
            if (elementConstraints.isEmpty()) {
                return null;
            }
    
            StringJoiner joiner = new StringJoiner(", ");
            elementConstraints.forEach(joiner::add);
            return "each element " + joiner;
        }
}
