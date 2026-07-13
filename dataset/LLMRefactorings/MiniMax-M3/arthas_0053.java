public class arthas_0053 {

        private static String addLineNumber(String src, Map<Integer, Integer> lineMapping) {
            int maxLineNumber = computeMaxLineNumber(lineMapping);
    
            StringBuilder sb = new StringBuilder();
    
            List<String> lines = StringUtils.toLines(src);
    
            String[] formatInfo = resolveFormatInfo(maxLineNumber);
            String formatStr = formatInfo[0];
            String emptyStr = formatInfo[1];
    
            int index = 0;
            for (String line : lines) {
                Integer srcLineNumber = lineMapping.get(index + 1);
                if (srcLineNumber != null) {
                    sb.append(String.format(formatStr, srcLineNumber));
                } else {
                    sb.append(emptyStr);
                }
                sb.append(line).append("\n");
                index++;
            }
    
            return sb.toString();
        }

        private static int computeMaxLineNumber(Map<Integer, Integer> lineMapping) {
            int maxLineNumber = 0;
            for (Integer value : lineMapping.values()) {
                if (value != null && value > maxLineNumber) {
                    maxLineNumber = value;
                }
            }
            return maxLineNumber;
        }

        private static String[] resolveFormatInfo(int maxLineNumber) {
            String formatStr = "/*%2d*/ ";
            String emptyStr = "       ";
    
            if (maxLineNumber >= 1000) {
                formatStr = "/*%4d*/ ";
                emptyStr = "         ";
            } else if (maxLineNumber >= 100) {
                formatStr = "/*%3d*/ ";
                emptyStr = "        ";
            }
    
            return new String[]{formatStr, emptyStr};
        }
}
