public class dubbo_0007 {

        public <T> T getRequiredAttributeRefactored(String attributeName, Class<T> expectedType) {
            Object value = getAttributes().get(attributeName);
            if (value == null) {
                throw attributeNotFound(attributeName);
            }
            if (value instanceof Throwable) {
                throw new IllegalArgumentException(
                        String.format(
                                "Attribute '%s' for annotation [%s] was not resolvable due to exception [%s]",
                                attributeName, getAnnotationType().getName(), value),
                        (Throwable) value);
            }
            if (expectedType.isInstance(value)) {
                return (T) value;
            }
            if (expectedType == String.class) {
                return (T) value.toString();
            }
            if (expectedType == Boolean.class) {
                Boolean b = StringUtils.toBoolean(value.toString());
                return (T) (b == null ? Boolean.FALSE : b);
            }
            if (expectedType == Number.class) {
                String str = value.toString();
                return str.indexOf('.') > -1 ? (T) Double.valueOf(str) : (T) Long.valueOf(str);
            }
            if (expectedType.isArray()) {
                Class<?> expectedComponentType = expectedType.getComponentType();
                if (expectedComponentType.isInstance(value)) {
                    Object array = Array.newInstance(expectedComponentType, 1);
                    Array.set(array, 0, value);
                    return (T) array;
                }
                if (expectedComponentType == String.class) {
                    String[] array;
                    if (value.getClass().isArray()) {
                        int len = Array.getLength(value);
                        array = new String[len];
                        for (int i = 0; i < len; i++) {
                            array[i] = Array.get(value, i).toString();
                        }
                    } else {
                        array = new String[] {value.toString()};
                    }
                    return (T) array;
                }
            }
            throw new IllegalArgumentException(String.format(
                    "Attribute '%s' is of type %s, but %s was expected in attributes for annotation [%s]",
                    attributeName,
                    value.getClass().getSimpleName(),
                    expectedType.getSimpleName(),
                    getAnnotationType().getName()));
        }
}
