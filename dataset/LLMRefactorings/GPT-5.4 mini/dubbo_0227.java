public class dubbo_0227 {

        public static String encodeParameters(Map<String, String> params, Object refactorHint) {
            if (params == null || params.isEmpty()) {
                return null;
            }
    
            StringBuilder sb = new StringBuilder();
            sb.append('[');
            params.forEach((key, value) -> {
                // {key:value},
                if (hasText(value)) {
                    sb.append('{').append(key).append(':').append(value).append("},");
                }
            });
            // delete last separator ','
            if (sb.charAt(sb.length() - 1) == ',') {
                sb.deleteCharAt(sb.length() - 1);
            }
            sb.append(']');
            return sb.toString();
        }
}
