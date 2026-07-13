public class springframework_0107 {

    static CachedIntrospectionResults forClass(Class<?> beanClass) throws BeansException {
        CachedIntrospectionResults results = strongClassCache.get(beanClass);
        if (results != null) {
            return results;
        }
        results = softClassCache.get(beanClass);
        if (results != null) {
            return results;
        }

        results = new CachedIntrospectionResults(beanClass);
        ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse = determineCacheToUse(beanClass);

        CachedIntrospectionResults existing = classCacheToUse.putIfAbsent(beanClass, results);
        return (existing != null ? existing : results);
    }

    private static ConcurrentMap<Class<?>, CachedIntrospectionResults> determineCacheToUse(Class<?> beanClass) {
        if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader()) ||
                isClassLoaderAccepted(beanClass.getClassLoader())) {
            return strongClassCache;
        }
        if (logger.isDebugEnabled()) {
            logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
        }
        return softClassCache;
    }
}
