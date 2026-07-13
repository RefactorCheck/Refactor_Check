public class dubbo_0227 {

    public static String encodeParameters(Map<String, String> params) {
        if (params == null || params.isEmpty()) {
            return null;
        }

        StringBuilder sb = new StringBuilder();
        sb.append('[');
        params.forEach((key, value) -> {
            if (hasText(value)) {
                sb.append('{').append(key).append(':').append(value).append("},");
            }
        });
        removeTrailingComma(sb);
        sb.append(']');
        return sb.toString();
    }

    private static void removeTrailingComma(StringBuilder sb) {
        if (sb.charAt(sb.length() - 1) == ',') {
            sb.deleteCharAt(sb.length() - 1);
        }
    }
}
