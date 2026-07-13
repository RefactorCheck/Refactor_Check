public class dubbo_0126 {

        @Override
        public Integer getNumberAsInteger(Map<String, ?> obj, String key) {
            assert obj != null;
            assert key != null;
            if (!obj.containsKey(key)) {
                return null;
            }
            Object value = obj.get(key);
            if (value instanceof Double) {
                return convertDoubleToInteger((Double) value);
            }
            if (value instanceof String) {
                try {
                    return Integer.parseInt((String) value);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException(
                            String.format("value '%s' for key '%s' is not an integer", value, key));
                }
            }
            throw new IllegalArgumentException(String.format("value '%s' for key '%s' is not an integer", value, key));
        }

        private Integer convertDoubleToInteger(Double d) {
            int i = d.intValue();
            if (i != d) {
                throw new ClassCastException("Number expected to be integer: " + d);
            }
            return i;
        }
}
