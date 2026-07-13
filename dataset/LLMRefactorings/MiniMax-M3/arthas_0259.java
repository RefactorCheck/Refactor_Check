public class arthas_0259 {

        @Override
        public boolean canConvert(Class<?> sourceType, Class<?> targetType) {
            if (sourceType == targetType) {
                return true;
            }
    
            if (targetType.isPrimitive()) {
                targetType = objectiveClass(targetType);
            }
    
            if (hasConverter(sourceType, targetType)) {
                return true;
            }
            if (targetType.isEnum()) {
                if (hasConverter(sourceType, Enum.class)) {
                    return true;
                }
            }
    
            if (targetType.isArray()) {
                return true;
            }
            return false;
        }

        private boolean hasConverter(Class<?> sourceType, Class<?> targetType) {
            return converters.containsKey(new ConvertiblePair(sourceType, targetType));
        }
}
