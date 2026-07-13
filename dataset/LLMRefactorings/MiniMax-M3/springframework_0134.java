public class springframework_0134 {

    	@Override
    	public PatternsRequestCondition combine(PatternsRequestCondition other) {
    		if (isEmptyPathMapping() && other.isEmptyPathMapping()) {
    			return new PatternsRequestCondition(ROOT_PATH_PATTERNS);
    		}
    		else if (other.isEmptyPathMapping()) {
    			return this;
    		}
    		else if (isEmptyPathMapping()) {
    			return other;
    		}
    		return new PatternsRequestCondition(combinePatterns(other), this);
    	}

    	private Set<String> combinePatterns(PatternsRequestCondition other) {
    		Set<String> result = new LinkedHashSet<>();
    		if (!this.patterns.isEmpty() && !other.patterns.isEmpty()) {
    			for (String pattern1 : this.patterns) {
    				for (String pattern2 : other.patterns) {
    					result.add(this.pathMatcher.combine(pattern1, pattern2));
    				}
    			}
    		}
    		return result;
    	}
}
