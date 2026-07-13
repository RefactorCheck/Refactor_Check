public class dubbo_0145 {

    private static final String JSESSIONID = "jsessionid";

    public static Map<String, List<String>> parseMatrixVariables(String matrixVariables) {
        Map<String, List<String>> result = null;
        StringTokenizer pairs = new StringTokenizer(matrixVariables, ";");
        while (pairs.hasMoreTokens()) {
            String pair = pairs.nextToken();
            int index = pair.indexOf('=');
            if (index == -1) {
                if (result == null) {
                    result = new LinkedHashMap<>();
                }
                result.computeIfAbsent(pair, k -> new ArrayList<>()).add(StringUtils.EMPTY_STRING);
                continue;
            }
            String name = pair.substring(0, index);
            if (JSESSIONID.equalsIgnoreCase(name)) {
                continue;
            }
            if (result == null) {
                result = new LinkedHashMap<>();
            }
            for (String value : StringUtils.tokenize(pair.substring(index + 1), ',')) {
                result.computeIfAbsent(name, k -> new ArrayList<>()).add(decodeURL(value));
            }
        }
        return result;
    }
}
