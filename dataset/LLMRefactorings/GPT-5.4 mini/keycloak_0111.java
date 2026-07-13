public class keycloak_0111 {

        public static boolean isAssignableFrom(Class<?> rawType1, Type[] actualTypeArguments1, Type candidateType) {
            if (candidateType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) candidateType;
                if (parameterizedType.getRawType() instanceof Class<?>) {
                    if (isAssignableFrom(rawType1, actualTypeArguments1, (Class<?>) parameterizedType.getRawType(),
                            parameterizedType.getActualTypeArguments())) {
                        return true;
                    }
                }
            } else if (candidateType instanceof Class<?>) {
                Class<?> clazz = (Class<?>) candidateType;
                if (isAssignableFrom(rawType1, actualTypeArguments1, clazz, EMPTY_TYPES)) {
                    return true;
                }
            } else if (candidateType instanceof TypeVariable<?>) {
                TypeVariable<?> typeVariable = (TypeVariable<?>) candidateType;
                if (isTypeBounded(rawType1, actualTypeArguments1, typeVariable.getBounds())) {
                    return true;
                }
            }
            return false;
        }
}
