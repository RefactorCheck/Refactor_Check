public class dubbo_0069 {

        @Override
        public Double getNumberAsDouble(Map<String, ?> obj, String key) {
            assert obj != null;
            assert key != null;
            if (!obj.containsKey(key)) {
                return null;
            }
            Object value = obj.get(key);
            if (value instanceof Double) {
                return (Double) value;
            }
            if (value instanceof String) {
                return parseDoubleValue((String) value, key);
            }
            throw new IllegalArgumentException(
                    String.format("value '%s' for key '%s' in '%s' is not a number", value, key, obj));
        }

        private Double parseDoubleValue(String value, String key) {
            try {
                return Double.parseDouble(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        String.format("value '%s' for key '%s' is not a double", value, key));
            }
        }
}
