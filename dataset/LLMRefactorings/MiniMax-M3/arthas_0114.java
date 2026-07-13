public class arthas_0114 {

    public static String nullSafeToString(float[] array) {
        if (array == null) {
            return "null";
        }
        return arrayToString(array);
    }

    private static String arrayToString(float[] array) {
        int length = array.length;
        if (length == 0) {
            return "{}";
        }
        StringBuilder sb = new StringBuilder("{");

        for (int i = 0; i < length; ++i) {
            if (i > 0) {
                sb.append(", ");
            }

            sb.append(array[i]);
        }

        sb.append("}");
        return sb.toString();
    }
}
