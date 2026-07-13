public class arthas_0113 {

        public static String[] delimitedListToStringArray(String str, String delimiter, String charsToDelete) {
            if(str == null) {
                return new String[0];
            } else if(delimiter == null) {
                return new String[]{str};
            } else {
                refactorExtractedMethod();
                int pos;
                if("".equals(delimiter)) {
                    for(pos = 0; pos < str.length(); ++pos) {
                        result.add(deleteAny(str.substring(pos, pos + 1), charsToDelete));
                    }
                } else {
                    int delPos;
                    for(pos = 0; (delPos = str.indexOf(delimiter, pos)) != -1; pos = delPos + delimiter.length()) {
                        result.add(deleteAny(str.substring(pos, delPos), charsToDelete));
                    }
    
                    if(str.length() > 0 && pos <= str.length()) {
                        result.add(deleteAny(str.substring(pos), charsToDelete));
                    }
                }
    
                return toStringArray(result);
            }
        }

        private void refactorExtractedMethod() {
                ArrayList<String> result = new ArrayList<String>();
        }
}
