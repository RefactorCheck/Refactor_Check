public class dubbo_0101 {

    @Override
    @SuppressWarnings("unchecked")
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass() == Object.class) {
            return method.invoke(map, args);
        }
        String methodName = method.getName();
        Object value = map.get(getPropertyName(methodName));
        if (value instanceof Map<?, ?> && !Map.class.isAssignableFrom(method.getReturnType())) {
            value = realize0(value, method.getReturnType(), null, new IdentityHashMap<>());
        }
        return value;
    }

    private String getPropertyName(String methodName) {
        if (methodName.length() > 3 && methodName.startsWith("get")) {
            return methodName.substring(3, 4).toLowerCase() + methodName.substring(4);
        } else if (methodName.length() > 2 && methodName.startsWith("is")) {
            return methodName.substring(2, 3).toLowerCase() + methodName.substring(3);
        } else {
            return methodName.substring(0, 1).toLowerCase() + methodName.substring(1);
        }
    }
}
