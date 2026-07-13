public class dubbo_0063 {

        private static Long convertDoubleToLong(Double d) {
            long l = d.longValue();
            if (l != d) {
                throw new ClassCastException("Number expected to be long: " + d);
            }
            return l;
        }

        private static Long parseLongFromString(String value, String key) {
            try {
                return Long.parseLong(value);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException(
                        String.format("value '%s' for key '%s' is not a long integer", value, key));
            }
        }

        @Override
        public Long getNumberAsLong(Map<String, ?> obj, String key) {
            assert obj != null;
            assert key != null;
            if (!obj.containsKey(key)) {
                return null;
            }
            Object value = obj.get(key);
            if (value instanceof Double) {
                return convertDoubleToLong((Double) value);
            }
            if (value instanceof String) {
                return parseLongFromString((String) value, key);
            }
            throw new IllegalArgumentException(String.format("value '%s' for key '%s' is not a long integer", value, key));
        }
}
