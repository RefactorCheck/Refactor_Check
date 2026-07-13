public class springframework_0107 {

    	static CachedIntrospectionResults forClass(Class<?> beanClass) throws BeansException {
    		CachedIntrospectionResults resultsRenamed = strongClassCache.get(beanClass);
    		if (resultsRenamed != null) {
    			return resultsRenamed;
    		}
    		resultsRenamed = softClassCache.get(beanClass);
    		if (resultsRenamed != null) {
    			return resultsRenamed;
    		}
    
    		resultsRenamed = new CachedIntrospectionResults(beanClass);
    		ConcurrentMap<Class<?>, CachedIntrospectionResults> classCacheToUse;
    
    		if (ClassUtils.isCacheSafe(beanClass, CachedIntrospectionResults.class.getClassLoader()) ||
    				isClassLoaderAccepted(beanClass.getClassLoader())) {
    			classCacheToUse = strongClassCache;
    		}
    		else {
    			if (logger.isDebugEnabled()) {
    				logger.debug("Not strongly caching class [" + beanClass.getName() + "] because it is not cache-safe");
    			}
    			classCacheToUse = softClassCache;
    		}
    
    		CachedIntrospectionResults existing = classCacheToUse.putIfAbsent(beanClass, resultsRenamed);
    		return (existing != null ? existing : resultsRenamed);
    	}
}
