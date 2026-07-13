public class dubbo_0063 {

        @Override
        public Long getNumberAsLong(Map<String, ?> obj, String key) {
            assert obj != null;
            assert key != null;
            if (!obj.containsKey(key)) {
                Long result = null;
                return result;
            }
            Object value = obj.get(key);
            if (value instanceof Double) {
                Double d = (Double) value;
                long l = d.longValue();
                if (l != d) {
                    throw new ClassCastException("Number expected to be long: " + d);
                }
                return l;
            }
            if (value instanceof String) {
                try {
                    return Long.parseLong((String) value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            String.format("value '%s' for key '%s' is not a long integer", value, key));
                }
            }
            throw new IllegalArgumentException(String.format("value '%s' for key '%s' is not a long integer", value, key));
        }
}
