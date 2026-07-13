public class keycloak_0111 {

        public static boolean isAssignableFrom(Class<?> rawType1, Type[] actualTypeArguments1, Type type2) {
            if (type2 instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) type2;
                if (parameterizedType.getRawType() instanceof Class<?>) {
                    if (isAssignableFrom(rawType1, actualTypeArguments1, (Class<?>) parameterizedType.getRawType(),
                            parameterizedType.getActualTypeArguments())) {
                        return true;
                    }
                }
            } else if (type2 instanceof Class<?>) {
                Class<?> clazz = (Class<?>) type2;
                if (isAssignableFrom(rawType1, actualTypeArguments1, clazz, EMPTY_TYPES)) {
                    return true;
                }
            } else if (type2 instanceof TypeVariable<?>) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) type2;
                if (isTypeBounded(rawType1, actualTypeArguments1, typeVariable.getBounds())) {
                    return true;
                }
            }
            return false;
        }
}
