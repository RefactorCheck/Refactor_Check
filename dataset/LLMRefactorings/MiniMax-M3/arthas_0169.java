public class arthas_0169 {

    public static String replace(String inString, String oldPattern, String newPattern) {
        if (hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
            return doReplace(inString, oldPattern, newPattern);
        } else {
            return inString;
        }
    }

    private static String doReplace(String inString, String oldPattern, String newPattern) {
        int pos = 0;
        int index = inString.indexOf(oldPattern);
        if (index < 0) {
            return inString;
        }

        int patLen = oldPattern.length();
        StringBuilder sb = new StringBuilder();
        for (; index >= 0; index = inString.indexOf(oldPattern, pos)) {
            sb.append(inString, pos, index);
            sb.append(newPattern);
            pos = index + patLen;
        }

        sb.append(inString.substring(pos));
        return sb.toString();
    }
}
