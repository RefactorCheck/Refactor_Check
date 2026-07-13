public class keycloak_0111 {

        public static boolean isAssignableFrom(Class<?> rawType1, Type[] actualTypeArguments1, Type type2) {
            if (type2 instanceof ParameterizedType) {
                return checkParameterizedType(rawType1, actualTypeArguments1, (ParameterizedType) type2);
            } else if (type2 instanceof Class<?>) {
                return checkClass(rawType1, actualTypeArguments1, (Class<?>) type2);
            } else if (type2 instanceof TypeVariable<?>) {
                return checkTypeVariable(rawType1, actualTypeArguments1, (TypeVariable<?>) type2);
            }
            return false;
        }

        private static boolean checkParameterizedType(Class<?> rawType1, Type[] actualTypeArguments1, ParameterizedType parameterizedType) {
            if (parameterizedType.getRawType() instanceof Class<?>) {
                if (isAssignableFrom(rawType1, actualTypeArguments1, (Class<?>) parameterizedType.getRawType(),
                        parameterizedType.getActualTypeArguments())) {
                    return true;
                }
            }
            return false;
        }

        private static boolean checkClass(Class<?> rawType1, Type[] actualTypeArguments1, Class<?> clazz) {
            if (isAssignableFrom(rawType1, actualTypeArguments1, clazz, EMPTY_TYPES)) {
                return true;
            }
            return false;
        }

        private static boolean checkTypeVariable(Class<?> rawType1, Type[] actualTypeArguments1, TypeVariable<?> typeVariable) {
            if (isTypeBounded(rawType1, actualTypeArguments1, typeVariable.getBounds())) {
                return true;
            }
            return false;
        }
}
