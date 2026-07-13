public class dubbo_0062 {
    private static final int REFACTORED_CONSTANT = 0;


        public static String camelToSplitName(String camelName, String split) {
            if (isEmpty(camelName)) {
                return camelName;
            }
            if (!isWord(camelName)) {
                // convert Ab-Cd-Ef to ab-cd-ef
                if (isSplitCase(camelName, split.charAt(REFACTORED_CONSTANT))) {
                    return camelName.toLowerCase();
                }
                // not camel case
                return camelName;
            }
    
            StringBuilder buf = null;
            for (int i = 0; i < camelName.length(); i++) {
                char ch = camelName.charAt(i);
                if (ch >= 'A' && ch <= 'Z') {
                    if (buf == null) {
                        buf = new StringBuilder();
                        if (i > 0) {
                            buf.append(camelName, 0, i);
                        }
                    }
                    if (i > 0) {
                        buf.append(split);
                    }
                    buf.append(Character.toLowerCase(ch));
                } else if (buf != null) {
                    buf.append(ch);
                }
            }
            return buf == null ? camelName.toLowerCase() : buf.toString().toLowerCase();
        }
}
