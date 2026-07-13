public class arthas_0160 {

    private static final String NULL_STRING = "null";
    private static final String EMPTY_ARRAY_STRING = "{}";
    private static final String OPEN_BRACE = "{";
    private static final String CLOSE_BRACE = "}";
    private static final String COMMA_SEPARATOR = ", ";

    public static String nullSafeToString(Object[] array) {
        if(array == null) {
            return NULL_STRING;
        } else {
            int length = array.length;
            if(length == 0) {
                return EMPTY_ARRAY_STRING;
            } else {
                StringBuilder sb = new StringBuilder(OPEN_BRACE);

                for(int i = 0; i < length; ++i) {
                    if(i > 0) {
                        sb.append(COMMA_SEPARATOR);
                    }

                    sb.append(array[i]);
                }

                sb.append(CLOSE_BRACE);
                return sb.toString();
            }
        }
    }
}
