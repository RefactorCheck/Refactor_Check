public class arthas_0175 {

    private static final String ARRAY_SUFFIX = "[]";
    private static final char ARRAY_MARKER = '[';

    static String normalizeArrayClassName(String className) {
        if (className == null || !className.endsWith(ARRAY_SUFFIX)) {
            return className;
        }
        int dimensions = 0;
        String base = className;
        while (base.endsWith(ARRAY_SUFFIX)) {
            dimensions++;
            base = base.substring(0, base.length() - 2);
        }
        if (base.isEmpty()) {
            return className;
        }
        String internal;
        if ("boolean".equals(base)) {
            internal = "Z";
        } else if ("byte".equals(base)) {
            internal = "B";
        } else if ("char".equals(base)) {
            internal = "C";
        } else if ("short".equals(base)) {
            internal = "S";
        } else if ("int".equals(base)) {
            internal = "I";
        } else if ("long".equals(base)) {
            internal = "J";
        } else if ("float".equals(base)) {
            internal = "F";
        } else if ("double".equals(base)) {
            internal = "D";
        } else {
            internal = "L" + base + ";";
        }
        StringBuilder sb = new StringBuilder(dimensions + internal.length());
        for (int i = 0; i < dimensions; i++) {
            sb.append(ARRAY_MARKER);
        }
        sb.append(internal);
        return sb.toString();
    }
}
