public class dubbo_0163 {

        @Override
        public void onResponse(Result appResponse, Invoker<?> invoker, Invocation invocation, Object refactorHint) {
            if (!invocation.getMethodName().startsWith("$") && !appResponse.hasException()) {
                Object value = appResponse.getValue();
                if (value != null) {
                    try {
                        Method method = invoker.getInterface()
                                .getMethod(invocation.getMethodName(), invocation.getParameterTypes());
                        Class<?> type = method.getReturnType();
                        Object newValue;
                        String serialization = UrlUtils.serializationOrDefault(invoker.getUrl());
                        if ("json".equals(serialization) || "fastjson".equals(serialization)) {
                            // If the serialization key is json or fastjson
                            Type gtype = method.getGenericReturnType();
                            newValue = PojoUtils.realize(value, type, gtype);
                        } else if (!type.isInstance(value)) {
                            // if local service interface's method's return type is not instance of return value
                            newValue = PojoUtils.isPojo(type)
                                    ? PojoUtils.realize(value, type)
                                    : CompatibleTypeUtils.compatibleTypeConvert(value, type);
    
                        } else {
                            newValue = value;
                        }
                        if (newValue != value) {
                            appResponse.setValue(newValue);
                        }
                    } catch (Throwable t) {
                        logger.warn(CONFIG_FILTER_VALIDATION_EXCEPTION, "", "", t.getMessage(), t);
                    }
                }
            }
        }
}
