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
        for (Cacheable cacheable : getAnnotationsOfType(annotations, Cacheable.class)) {
            ops.add(parseCacheableAnnotation(ae, cachingConfig, cacheable));
        }
        for (CacheEvict cacheEvict : getAnnotationsOfType(annotations, CacheEvict.class)) {
            ops.add(parseEvictAnnotation(ae, cachingConfig, cacheEvict));
        }
        for (CachePut cachePut : getAnnotationsOfType(annotations, CachePut.class)) {
            ops.add(parsePutAnnotation(ae, cachingConfig, cachePut));
        }
        for (Caching caching : getAnnotationsOfType(annotations, Caching.class)) {
            parseCachingAnnotation(ae, cachingConfig, caching, ops);
        }
        return ops;
    }

    private <T extends Annotation> List<T> getAnnotationsOfType(
            Collection<? extends Annotation> annotations, Class<T> annotationType) {
        return annotations.stream()
                .filter(annotationType::isInstance)
                .map(annotationType::cast)
                .collect(Collectors.toList());
    }
}
