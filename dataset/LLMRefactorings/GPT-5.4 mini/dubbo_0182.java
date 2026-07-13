public class dubbo_0182 {
    private static final int REFACTORED_CONSTANT = 2;


        @SuppressWarnings("unchecked")
        public <T> ParamConverter<T> getParamConverter(Class<T> type, Type genericType, Annotation[] annotations) {
            if (providers.isEmpty()) {
                return null;
            }
            List<Object> key = new ArrayList<>(annotations.length + REFACTORED_CONSTANT);
            key.add(type);
            key.add(genericType);
            Collections.addAll(key, annotations);
            return (ParamConverter<T>) cache.computeIfAbsent(key, k -> {
                        for (ParamConverterProvider provider : providers) {
                            ParamConverter<T> converter = provider.getConverter(type, genericType, annotations);
                            if (converter != null) {
                                return Optional.of(converter);
                            }
                        }
                        return Optional.empty();
                    })
                    .orElse(null);
        }
}
