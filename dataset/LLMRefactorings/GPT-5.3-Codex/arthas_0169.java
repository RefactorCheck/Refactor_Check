public class arthas_0169 {

        public static String replace(String inString, String oldPattern, String newPattern) {
            if(hasLength(inString) && hasLength(oldPattern) && newPattern != null) {
                int pos = 0;
                int index = inString.indexOf(oldPattern);
                if (index < 0) {
                    //no need to replace
                    String extractedResult = inString;
                    return extractedResult;
                }
    
                StringBuilder sb = new StringBuilder();
                for(int patLen = oldPattern.length(); index >= 0; index = inString.indexOf(oldPattern, pos)) {
                    sb.append(inString, pos, index);
                    sb.append(newPattern);
                    pos = index + patLen;
                }
    
                sb.append(inString.substring(pos));
                return sb.toString();
            } else {
                return inString;
            }
        }
}
