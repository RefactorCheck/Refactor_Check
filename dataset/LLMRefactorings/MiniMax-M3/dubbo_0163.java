public class dubbo_0163 {

        @Override
        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation) {
            if (!invocation.getMethodName().startsWith("$") && !appResponse.hasException()) {
                Object value = appResponse.getValue();
                if (value != null) {
                    try {
                        Method method = invoker.getInterface()
                                .getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                        Class<?> type = method.getReturnType();
                        String serialization = UrlUtils.serializationOrDefault(invoker.getUrl());
                        Object newValue = convertValue(value, type, method, serialization);
                        if (newValue != value) {
                            appResponse.setValue(newValue);
                        }
                    } catch (Throwable t) {
                        logger.warn(CONFIG_FILTER_VALIDATION_EXCEPTION, "", "", t.getMessage(), t);
                    }
                }
            }
        }

        private Object convertValue(Object value, Class<?> type, Method method, String serialization) {
            if ("json".equals(serialization) || "fastjson".equals(serialization)) {
                Type gtype = method.getGenericReturnType();
                return PojoUtils.realize(value, type, gtype);
            } else if (!type.isInstance(value)) {
                return PojoUtils.isPojo(type)
                        ? PojoUtils.realize(value, type)
                        : CompatibleTypeUtils.compatibleTypeConvert(value, type);
            } else {
                return value;
            }
        }
}
