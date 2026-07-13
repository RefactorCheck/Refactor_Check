public static class dubbo_0210 {

        public Object convert(Object value, Class<?> type) {
            if (value == null) {
                return null;
            }
    
            if (type.isInstance(value)) {
                return value;
            }
    
            TypeParameterMeta parameter = new TypeParameterMeta(type);
            List<ArgumentConverter> converters = getSuitableConverters(value.getClass(), type);
            Object target;
            for (int i = 0, size = converters.size(); i < size; i++) {
                target = converters.get(i).convert(value, parameter);
                if (target != null) {
                    return target;
                }
            }
    
            return null;
        }
}
