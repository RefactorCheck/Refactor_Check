public class dubbo_0140 {

        private void validate(Set<ConstraintViolation<?>> violations, Object arg, Class<?>... groups) {
            if (arg != null && !ReflectUtils.isPrimitives(arg.getClass())) {
                if (arg instanceof Object[]) {
                    for (Object item : (Object[]) arg) {
                        validate(violations, item, groups);
                    }
                } else if (arg instanceof Collection) {
                    for (Object item : (Collection<?>) arg) {
                        validate(violations, item, groups);
                    }
                } else if (arg instanceof Map) {
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) arg).entrySet()) {
                        validate(violations, entry.getKey(), groups);
                        validate(violations, entry.getValue(), groups);
                    }
                } else {
                    validateSingle(violations, arg, groups);
                }
            }
        }

        private void validateSingle(Set<ConstraintViolation<?>> violations, Object arg, Class<?>... groups) {
            violations.addAll(validator.validate(arg, groups));
        }
}
