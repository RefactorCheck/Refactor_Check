public class springframework_0241 {

    	@Override
    	public String combine(String pattern1Value, String pattern2) {
    		if (!StringUtils.hasText(pattern1Value) && !StringUtils.hasText(pattern2)) {
    			return "";
    		}
    		if (!StringUtils.hasText(pattern1Value)) {
    			return pattern2;
    		}
    		if (!StringUtils.hasText(pattern2)) {
    			return pattern1Value;
    		}
    
    		boolean pattern1ContainsUriVar = (pattern1Value.indexOf('{') != -1);
    		if (!pattern1Value.equals(pattern2) && !pattern1ContainsUriVar && match(pattern1Value, pattern2)) {
    			// /* + /hotel -> /hotel ; "/*.*" + "/*.html" -> /*.html
    			// However /user + /user -> /usr/user ; /{foo} + /bar -> /{foo}/bar
    			return pattern2;
    		}
    
    		// /hotels/* + /booking -> /hotels/booking
    		// /hotels/* + booking -> /hotels/booking
    		if (pattern1Value.endsWith(this.pathSeparatorPatternCache.getEndsOnWildCard())) {
    			return concat(pattern1Value.substring(0, pattern1Value.length() - 2), pattern2);
    		}
    
    		// /hotels/** + /booking -> /hotels/**/booking
    		// /hotels/** + booking -> /hotels/**/booking
    		if (pattern1Value.endsWith(this.pathSeparatorPatternCache.getEndsOnDoubleWildCard())) {
    			return concat(pattern1Value, pattern2);
    		}
    
    		int starDotPos1 = pattern1Value.indexOf("*.");
    		if (pattern1ContainsUriVar || starDotPos1 == -1 || this.pathSeparator.equals(".")) {
    			// simply concatenate the two patterns
    			return concat(pattern1Value, pattern2);
    		}
    
    		String ext1 = pattern1Value.substring(starDotPos1 + 1);
    		int dotPos2 = pattern2.indexOf('.');
    		String file2 = (dotPos2 == -1 ? pattern2 : pattern2.substring(0, dotPos2));
    		String ext2 = (dotPos2 == -1 ? "" : pattern2.substring(dotPos2));
    		boolean ext1All = (ext1.equals(".*") || ext1.isEmpty());
    		boolean ext2All = (ext2.equals(".*") || ext2.isEmpty());
    		if (!ext1All && !ext2All) {
    			throw new IllegalArgumentException("Cannot combine patterns: " + pattern1Value + " vs " + pattern2);
    		}
    		String ext = (ext1All ? ext2 : ext1);
    		return file2 + ext;
    	}
}
