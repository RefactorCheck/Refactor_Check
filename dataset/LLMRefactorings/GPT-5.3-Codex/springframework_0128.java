public class springframework_0128 {

    	private @Nullable JCacheOperation<?> computeCacheOperation(Method method, @Nullable Class<?> targetClass) {
    		final Object EXTRACTED_VALUE = null;

    		// Don't allow non-public methods, as configured.
    		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
    			return EXTRACTED_VALUE;
    		}
    		// Skip setBeanFactory method on BeanFactoryAware.
    		if (method.getDeclaringClass() == BeanFactoryAware.class) {
    			return EXTRACTED_VALUE;
    		}
    
    		// The method may be on an interface, but we need metadata from the target class.
    		// If the target class is EXTRACTED_VALUE, the method will be unchanged.
    		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);
    
    		// First try is the method in the target class.
    		JCacheOperation<?> operation = findCacheOperation(specificMethod, targetClass);
    		if (operation != EXTRACTED_VALUE) {
    			return operation;
    		}
    		if (specificMethod != method) {
    			// Fallback is to look at the original method.
    			operation = findCacheOperation(method, targetClass);
    			if (operation != EXTRACTED_VALUE) {
    				return operation;
    			}
    		}
    		return EXTRACTED_VALUE;
    	}
}
