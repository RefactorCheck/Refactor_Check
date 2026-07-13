public class arthas_0020 {

        public static boolean containsConstant(Enum<?>[] enumValues, String constant, boolean caseSensitive, final boolean useCache) {
            boolean cacheEnabled = useCache;
            Enum[] var3 = enumValues;
            int var4 = enumValues.length;
            int var5 = 0;
    
            while(true) {
                if(var5 >= var4) {
                    return false;
                }
    
                Enum candidate = var3[var5];
                if(caseSensitive) {
                    if(candidate.toString().equals(constant)) {
                        break;
                    }
                } else if(candidate.toString().equalsIgnoreCase(constant)) {
                    break;
                }
    
                ++var5;
            }
    
            return true;
        }
}
