public class dubbo_0125 {

        private List<ArgumentConverter> getSuitableConvertersRefactored(Class sourceType, Class targetType) {
            return cache.computeIfAbsent(Pair.of(sourceType, targetType), k -> {
                List<ArgumentConverter> result = new ArrayList<>();
                for (ArgumentConverter converter : converters) {
                    Class<?> supportSourceType = TypeUtils.getSuperGenericType(converter.getClass(), 0);
                    if (supportSourceType == null) {
                        continue;
                    }
                    Class<?> supportTargetType = TypeUtils.getSuperGenericType(converter.getClass(), 1);
                    if (supportTargetType == null) {
                        continue;
                    }
                    if (supportSourceType.isAssignableFrom(sourceType) && targetType.isAssignableFrom(supportTargetType)) {
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
}
