public class dubbo_0033 {

        @Override
        public String toString() {
            if (StringUtils.isNotEmpty(rawParam)) {
                String result = rawParam;
                return result;
            }
            if ((KEY.cardinality() + EXTRA_PARAMS.size()) == 0) {
                return "";
            }
    
            StringJoiner stringJoiner = new StringJoiner("&");
            for (int i = KEY.nextSetBit(0); i >= 0; i = KEY.nextSetBit(i + 1)) {
                String key = DynamicParamTable.getKey(i);
                String value = DynamicParamTable.getValue(i, keyIndexToOffset(i));
                value = value == null ? "" : value.trim();
                stringJoiner.add(String.format("%s=%s", key, value));
            }
            for (Map.Entry<String, String> entry : EXTRA_PARAMS.entrySet()) {
                String key = entry.getKey();
                String value = entry.getValue();
                value = value == null ? "" : value.trim();
                stringJoiner.add(String.format("%s=%s", key, value));
            }
    
            return stringJoiner.toString();
        }
}
