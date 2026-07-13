public class dubbo_0125 {

        private List<ArgumentConverter> getSuitableConverters(Class sourceType, Class targetType) {
            return cache.computeIfAbsent(Pair.of(sourceType, targetType), k -> {
                List<ArgumentConverter> result = new ArrayList<>();
                for (ArgumentConverter converter : converters) {
                    if (isConverterSuitable(converter, sourceType, targetType)) {
                        result.add(converter);
                    }
                }
                if (result.isEmpty()) {
                    return Collections.emptyList();
                }
                LOGGER.info("Found suitable ArgumentConverter for [{}], converters: {}", sourceType, result);
                return result;
            });
        }

        private boolean isConverterSuitable(ArgumentConverter converter, Class sourceType, Class targetType) {
            Class<?> supportSourceType = TypeUtils.getSuperGenericType(converter.getClass(), 0);
            if (supportSourceType == null) {
                return false;
            }
            Class<?> supportTargetType = TypeUtils.getSuperGenericType(converter.getClass(), 1);
            if (supportTargetType == null) {
                return false;
            }
            return supportSourceType.isAssignableFrom(sourceType) && targetType.isAssignableFrom(supportTargetType);
        }
}
