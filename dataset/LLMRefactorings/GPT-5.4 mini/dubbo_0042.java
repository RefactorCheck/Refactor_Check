public static class dubbo_0042 {

        @Override
        public Object convert(Object value, ParameterMeta parameter) {
            Class<?> type = parameter.getType();
            if (value == null) {
                return TypeUtils.nullDefault(type);
            }
    
            if (type.isInstance(value)) {
                if (parameter.getGenericType() instanceof Class) {
                    return value;
                }
                return parameter.getToolKit().convert(value, parameter);
            }
    
            List<ArgumentConverter> converters = getSuitableConverters(value.getClass(), type);
            Object target;
            for (int i = 0, size = converters.size(); i < size; i++) {
                target = converters.get(i).convert(value, parameter);
                if (target != null) {
                    return target;
                }
            }
    
            return parameter.getToolKit().convert(value, parameter);
        }
}
