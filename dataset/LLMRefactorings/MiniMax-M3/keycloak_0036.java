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
                collectTypeArgumentConstraints(typeArg, elementConstraints);
            }

            if (elementConstraints.isEmpty()) {
                return null;
            }

            StringJoiner joiner = new StringJoiner(", ");
            elementConstraints.forEach(joiner::add);
            return "each element " + joiner;
        }

        private void collectTypeArgumentConstraints(Type typeArg, List<String> elementConstraints) {
            for (AnnotationInstance annotation : typeArg.annotations()) {
                DotName name = annotation.name();
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
}
