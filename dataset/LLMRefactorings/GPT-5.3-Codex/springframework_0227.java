public class springframework_0227 {

    	private void parseResourceCache(ManagedList<Object> resourceResolversValue,
    			ManagedList<Object> resourceTransformers, Element element, @Nullable Object source) {
    
    		String resourceCache = element.getAttribute("resource-cache");
    		if ("true".equals(resourceCache)) {
    			ConstructorArgumentValues cargs = new ConstructorArgumentValues();
    
    			RootBeanDefinition cachingResolverDef = new RootBeanDefinition(CachingResourceResolver.class);
    			cachingResolverDef.setSource(source);
    			cachingResolverDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    			cachingResolverDef.setConstructorArgumentValues(cargs);
    
    			RootBeanDefinition cachingTransformerDef = new RootBeanDefinition(CachingResourceTransformer.class);
    			cachingTransformerDef.setSource(source);
    			cachingTransformerDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    			cachingTransformerDef.setConstructorArgumentValues(cargs);
    
    			String cacheManagerName = element.getAttribute("cache-manager");
    			String cacheName = element.getAttribute("cache-name");
    			if (StringUtils.hasText(cacheManagerName) && StringUtils.hasText(cacheName)) {
    				RuntimeBeanReference cacheManagerRef = new RuntimeBeanReference(cacheManagerName);
    				cargs.addIndexedArgumentValue(0, cacheManagerRef);
    				cargs.addIndexedArgumentValue(1, cacheName);
    			}
    			else {
    				ConstructorArgumentValues cacheCavs = new ConstructorArgumentValues();
    				cacheCavs.addIndexedArgumentValue(0, RESOURCE_CHAIN_CACHE);
    				RootBeanDefinition cacheDef = new RootBeanDefinition(ConcurrentMapCache.class);
    				cacheDef.setSource(source);
    				cacheDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    				cacheDef.setConstructorArgumentValues(cacheCavs);
    				cargs.addIndexedArgumentValue(0, cacheDef);
    			}
    			resourceResolversValue.add(cachingResolverDef);
    			resourceTransformers.add(cachingTransformerDef);
    		}
    	}
}
