public class dubbo_0075 {

        public static List<Class<?>> getClassGenerics(Class<?> clazz, Class<?> interfaceClass) {
            List<Class<?>> generics = new ArrayList<>();
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class && interfaceClass.isAssignableFrom((Class<?>) rawType)) {
                        addClassTypeArguments(generics, parameterizedType);
                    }
                }
            }
            Type genericSuperclass = clazz.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                addClassTypeArguments(generics, (ParameterizedType) genericSuperclass);
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                generics.addAll(getClassGenerics(superclass, interfaceClass));
            }
            return generics.stream().distinct().collect(Collectors.toList());
        }

        private static void addClassTypeArguments(List<Class<?>> generics, ParameterizedType parameterizedType) {
            Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
            for (Type actualTypeArgument : actualTypeArguments) {
                if (actualTypeArgument instanceof Class) {
                    generics.add((Class<?>) actualTypeArgument);
                }
            }
        }
}
