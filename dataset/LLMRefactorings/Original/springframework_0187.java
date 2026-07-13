public class springframework_0187 {

    	static <T extends TargetedAccessor> List<T> getAccessorsToTry(
    			@Nullable Class<?> targetType, List<T> accessors) {
    
    		if (accessors.isEmpty()) {
    			return Collections.emptyList();
    		}
    
    		List<T> exactMatches = new ArrayList<>();
    		List<T> inexactMatches = new ArrayList<>();
    		List<T> genericMatches = new ArrayList<>();
    		for (T accessor : accessors) {
    			Class<?>[] targets = accessor.getSpecificTargetClasses();
    			if (ObjectUtils.isEmpty(targets)) {
    				// generic accessor that says it can be used for any type
    				genericMatches.add(accessor);
    			}
    			else if (targetType != null) {
    				for (Class<?> clazz : targets) {
    					if (clazz == targetType) {
    						exactMatches.add(accessor);
    					}
    					else if (clazz.isAssignableFrom(targetType)) {
    						inexactMatches.add(accessor);
    					}
    				}
    			}
    		}
    
    		int size = exactMatches.size() + inexactMatches.size() + genericMatches.size();
    		if (size == 0) {
    			return Collections.emptyList();
    		}
    		else {
    			List<T> result = new ArrayList<>(size);
    			result.addAll(exactMatches);
    			result.addAll(inexactMatches);
    			result.addAll(genericMatches);
    			return result;
    		}
    	}
}
