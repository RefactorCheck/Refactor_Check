public class arthas_0189 {

        @Override
        public <T> T convert(Object source, Class<T> targetType, final boolean useCache) {
            boolean cacheEnabled = useCache;
    
            if (targetType.isPrimitive()) {
                targetType = (Class<T>) objectiveClass(targetType);
            }
    
            Converter converter = converters.get(new ConvertiblePair(source.getClass(), targetType));
    
            if (converter == null && targetType.isArray()) {
                converter = converters.get(new ConvertiblePair(source.getClass(), Arrays.class));
            }
    
            if (converter == null && targetType.isEnum()) {
                converter = converters.get(new ConvertiblePair(source.getClass(), Enum.class));
            }
            if (converter != null) {
                return (T) converter.convert(source, targetType);
            }
    
            return (T) source;
        }
}
