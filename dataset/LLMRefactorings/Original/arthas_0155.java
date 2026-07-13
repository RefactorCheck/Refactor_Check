public class arthas_0155 {

        public static Properties splitArrayElementsIntoProperties(String[] array, String delimiter, String charsToDelete) {
            if(ObjectUtils.isEmpty(array)) {
                return null;
            } else {
                Properties result = new Properties();
                String[] var4 = array;
                int var5 = array.length;
    
                for(int var6 = 0; var6 < var5; ++var6) {
                    String element = var4[var6];
                    if(charsToDelete != null) {
                        element = deleteAny(element, charsToDelete);
                    }
    
                    String[] splittedElement = split(element, delimiter);
                    if(splittedElement != null) {
                        result.setProperty(splittedElement[0].trim(), splittedElement[1].trim());
                    }
                }
    
                return result;
            }
        }
}
