public class dubbo_0182 {

        @SuppressWarnings("unchecked")
        public <T> ParamConverter<T> getParamConverter(Class<T> type, Type genericType, Annotation[] annotations) {
            if (providers.isEmpty()) {
                return null;
            }
            List<Object> key = buildCacheKey(type, genericType, annotations);
            return (ParamConverter<T>) cache.computeIfAbsent(key, k -> findConverter(type, genericType, annotations))
                    .orElse(null);
        }

        private <T> List<Object> buildCacheKey(Class<T> type, Type genericType, Annotation[] annotations) {
            List<Object> key = new ArrayList<>(annotations.length + 2);
            key.add(type);
            key.add(genericType);
            Collections.addAll(key, annotations);
            return key;
        }

        private <T> Optional<ParamConverter<T>> findConverter(Class<T> type, Type genericType, Annotation[] annotations) {
            for (ParamConverterProvider provider : providers) {
                ParamConverter<T> converter = provider.getConverter(type, genericType, annotations);
                if (converter != null) {
                    return Optional.of(converter);
                }
            }
            return Optional.empty();
        }
}
