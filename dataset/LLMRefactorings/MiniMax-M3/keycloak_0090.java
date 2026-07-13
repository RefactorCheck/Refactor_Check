public class keycloak_0090 {

    public static String resolveVariables(String text, Properties props, String startMarker, String endMarker) {
        int s = text.indexOf(startMarker);
        if (s == -1) {
            return text;
        }
        return resolve(text, props, startMarker, endMarker, s);
    }

    private static String resolve(String text, Properties props, String startMarker, String endMarker, int startPos) {
        int e = 0;
        int s = startPos;
        StringBuilder sb = new StringBuilder();
        do {
            if (e < s) {
                sb.append(text.substring(e, s));
            }
            e = text.indexOf(endMarker, s + startMarker.length());
            if (e != -1) {
                String key = text.substring(s + startMarker.length(), e);
                sb.append(props.getProperty(key, key));
                e += endMarker.length();
                s = text.indexOf(startMarker, e);
            } else {
                e = s;
                break;
            }
        } while (s != -1);
        if (e < text.length()) {
            sb.append(text.substring(e));
        }
        return sb.toString();
    }
}
