public class dubbo_0062 {

        public static String camelToSplitName(String camelName, String split) {
            if (isEmpty(camelName)) {
                return camelName;
            }
            if (!isWord(camelName)) {
                if (isSplitCase(camelName, split.charAt(0))) {
                    return camelName.toLowerCase();
                }
                return camelName;
            }
            return convertCamelToSplit(camelName, split);
        }

        private static String convertCamelToSplit(String camelName, String split) {
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
