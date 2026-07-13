public class springframework_0124 {

    	private @Nullable Collection<CacheOperation> computeCacheOperations(Method method, @Nullable Class<?> targetClass) {
    		// Don't allow non-public methods, as configured.
    		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
    			return null;
    		}
    		// Skip setBeanFactory method on BeanFactoryAware.
    		if (method.getDeclaringClass() == BeanFactoryAware.class) {
    			return null;
    		}
    
    		// The method may be on an interface, but we need metadata from the target class.
    		// If the target class is null, the method will be unchanged.
    		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
    
    		// First try is the method in the target class.
    		Collection<CacheOperation> opDef = findCacheOperations(specificMethod);
    		if (opDef != null) {
    			return opDef;
    		}
    
    		// Second try is the caching operation on the target class.
    		opDef = findCacheOperations(specificMethod.getDeclaringClass());
    		if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
    			return opDef;
    		}
    
    		if (specificMethod != method) {
    			// Fallback is to look at the original method.
    			opDef = findCacheOperations(method);
    			if (opDef != null) {
    				return opDef;
    			}
    			// Last fallback is the class of the original method.
    			opDef = findCacheOperations(method.getDeclaringClass());
    			if (opDef != null && ClassUtils.isUserLevelMethod(method)) {
    				return opDef;
    			}
    		}
    
    		return null;
    	}
}
