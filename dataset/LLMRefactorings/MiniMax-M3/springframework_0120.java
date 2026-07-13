public class springframework_0120 {

    	@Override
    	public PathPatternsRequestCondition combine(PathPatternsRequestCondition other) {
    		if (isEmptyPathMapping() && other.isEmptyPathMapping()) {
    			return new PathPatternsRequestCondition(ROOT_PATH_PATTERNS);
    		}
    		else if (other.isEmptyPathMapping()) {
    			return this;
    		}
    		else if (isEmptyPathMapping()) {
    			return other;
    		}
    		else {
    			return new PathPatternsRequestCondition(combinePatterns(other));
    		}
    	}

    	private SortedSet<PathPattern> combinePatterns(PathPatternsRequestCondition other) {
    		SortedSet<PathPattern> combined = new TreeSet<>();
    		for (PathPattern pattern1 : this.patterns) {
    			for (PathPattern pattern2 : other.patterns) {
    				combined.add(pattern1.combine(pattern2));
    			}
    		}
    		return combined;
    	}
}
