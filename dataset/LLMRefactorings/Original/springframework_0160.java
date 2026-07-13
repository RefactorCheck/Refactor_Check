public class springframework_0160 {

    	private static boolean simpleMatch(@Nullable String pattern, @Nullable String str, boolean ignoreCase) {
    		if (pattern == null || str == null) {
    			return false;
    		}
    
    		int firstIndex = pattern.indexOf('*');
    		if (firstIndex == -1) {
    			return (ignoreCase ? pattern.equalsIgnoreCase(str) : pattern.equals(str));
    		}
    
    		if (firstIndex == 0) {
    			if (pattern.length() == 1) {
    				return true;
    			}
    			int nextIndex = pattern.indexOf('*', 1);
    			if (nextIndex == -1) {
    				String part = pattern.substring(1);
    				return (ignoreCase ? StringUtils.endsWithIgnoreCase(str, part) : str.endsWith(part));
    			}
    			String part = pattern.substring(1, nextIndex);
    			if (part.isEmpty()) {
    				return simpleMatch(pattern.substring(nextIndex), str, ignoreCase);
    			}
    			int partIndex = indexOf(str, part, 0, ignoreCase);
    			while (partIndex != -1) {
    				if (simpleMatch(pattern.substring(nextIndex), str.substring(partIndex + part.length()), ignoreCase)) {
    					return true;
    				}
    				partIndex = indexOf(str, part, partIndex + 1, ignoreCase);
    			}
    			return false;
    		}
    
    		return (str.length() >= firstIndex &&
    				checkStartsWith(pattern, str, firstIndex, ignoreCase) &&
    				simpleMatch(pattern.substring(firstIndex), str.substring(firstIndex), ignoreCase));
    	}
}
