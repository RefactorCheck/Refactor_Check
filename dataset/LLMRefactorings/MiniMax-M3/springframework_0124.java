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
        Collection<CacheOperation> opDef = findCacheOperationsFor(specificMethod, method);
        if (opDef != null) {
            return opDef;
        }

        if (specificMethod != method) {
            // Fallback is to look at the original method.
            opDef = findCacheOperationsFor(method, method);
            if (opDef != null) {
                return opDef;
            }
        }

        return null;
    }

    private @Nullable Collection<CacheOperation> findCacheOperationsFor(Method methodToLookup, Method originalMethod) {
        Collection<CacheOperation> opDef = findCacheOperations(methodToLookup);
        if (opDef != null) {
            return opDef;
        }
        opDef = findCacheOperations(methodToLookup.getDeclaringClass());
        if (opDef != null && ClassUtils.isUserLevelMethod(originalMethod)) {
            return opDef;
        }
        return null;
    }
}
