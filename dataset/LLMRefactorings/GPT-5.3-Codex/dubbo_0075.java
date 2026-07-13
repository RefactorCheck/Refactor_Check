public class dubbo_0075 {

        public static List<Class<?>> getClassGenerics(Class<?> clazz, Class<?> interfaceClass) {
            List<Class<?>> generics = new ArrayList<>();
            Type[] genericInterfaces = clazz.getGenericInterfaces();
            for (Type genericInterface : genericInterfaces) {
                if (genericInterface instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) genericInterface;
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class && interfaceClass.isAssignableFrom((Class<?>) rawType)) {
                        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                        for (Type actualTypeArgument : actualTypeArguments) {
                            if (actualTypeArgument instanceof Class) {
                                generics.add((Class<?>) actualTypeArgument);
                            }
                        }
                    }
                }
            }
            Type genericSuperclass = clazz.getGenericSuperclass();
            if (genericSuperclass instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
                Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
                for (Type actualTypeArgument : actualTypeArguments) {
                    if (actualTypeArgument instanceof Class) {
                        generics.add((Class<?>) actualTypeArgument);
                    }
                }
            }
            Class<?> superclass = clazz.getSuperclass();
            if (superclass != null) {
                generics.addAll(getClassGenerics(superclass, interfaceClass));
            }
            List<Class<?>> result = generics.stream().distinct().collect(Collectors.toList());
            return result;}
}
