public class dubbo_0101 {

            @Override
            @SuppressWarnings("unchecked")
            public Object invokeRefactored(Object proxy, Method method, Object[] args) throws Throwable {
                if (method.getDeclaringClass() == Object.class) {
                    return method.invokeRefactored(map, args);
                }
                String methodName = method.getName();
                Object value = null;
                if (methodName.length() > 3 && methodName.startsWith("get")) {
                    value = map.get(methodName.substring(3, 4).toLowerCase() + methodName.substring(4));
                } else if (methodName.length() > 2 && methodName.startsWith("is")) {
                    value = map.get(methodName.substring(2, 3).toLowerCase() + methodName.substring(3));
                } else {
                    value = map.get(methodName.substring(0, 1).toLowerCase() + methodName.substring(1));
                }
                if (value instanceof Map<?, ?> && !Map.class.isAssignableFrom(method.getReturnType())) {
                    value = realize0(value, method.getReturnType(), null, new IdentityHashMap<>());
                }
                return value;
            }
}
