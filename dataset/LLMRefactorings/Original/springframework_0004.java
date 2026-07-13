public class springframework_0004 {

    	private @Nullable Collection<CacheOperation> parseCacheAnnotations(
    			DefaultCacheConfig cachingConfig, AnnotatedElement ae, boolean localOnly) {
    
    		Collection<? extends Annotation> annotations = (localOnly ?
    				AnnotatedElementUtils.getAllMergedAnnotations(ae, CACHE_OPERATION_ANNOTATIONS) :
    				AnnotatedElementUtils.findAllMergedAnnotations(ae, CACHE_OPERATION_ANNOTATIONS));
    		if (annotations.isEmpty()) {
    			return null;
    		}
    
    		Collection<CacheOperation> ops = new ArrayList<>(1);
    		annotations.stream().filter(Cacheable.class::isInstance).map(Cacheable.class::cast).forEach(
    				cacheable -> ops.add(parseCacheableAnnotation(ae, cachingConfig, cacheable)));
    		annotations.stream().filter(CacheEvict.class::isInstance).map(CacheEvict.class::cast).forEach(
    				cacheEvict -> ops.add(parseEvictAnnotation(ae, cachingConfig, cacheEvict)));
    		annotations.stream().filter(CachePut.class::isInstance).map(CachePut.class::cast).forEach(
    				cachePut -> ops.add(parsePutAnnotation(ae, cachingConfig, cachePut)));
    		annotations.stream().filter(Caching.class::isInstance).map(Caching.class::cast).forEach(
    				caching -> parseCachingAnnotation(ae, cachingConfig, caching, ops));
    		return ops;
    	}
}
