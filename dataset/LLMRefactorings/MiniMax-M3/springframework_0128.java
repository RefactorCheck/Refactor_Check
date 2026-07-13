public class springframework_0128 {

	private @Nullable JCacheOperation<?> computeCacheOperation(Method method, @Nullable Class<?> targetClass) {
		if (shouldSkipMethod(method)) {
			return null;
		}

		// The method may be on an interface, but we need metadata from the target class.
		// If the target class is null, the method will be unchanged.
		Method specificMethod = AopUtils.getMostSpecificMethod(method, targetClass);

		// First try is the method in the target class.
		JCacheOperation<?> operation = findCacheOperation(specificMethod, targetClass);
		if (operation != null) {
			return operation;
		}
		if (specificMethod != method) {
			// Fallback is to look at the original method.
			operation = findCacheOperation(method, targetClass);
			if (operation != null) {
				return operation;
			}
		}
		return null;
	}

	private boolean shouldSkipMethod(Method method) {
		// Don't allow non-public methods, as configured.
		if (allowPublicMethodsOnly() && !Modifier.isPublic(method.getModifiers())) {
			return true;
		}
		// Skip setBeanFactory method on BeanFactoryAware.
		if (method.getDeclaringClass() == BeanFactoryAware.class) {
			return true;
		}
		return false;
	}
}
