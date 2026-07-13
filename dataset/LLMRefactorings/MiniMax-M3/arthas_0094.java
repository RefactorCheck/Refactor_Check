public class arthas_0094 {

        public static synchronized EnhancerAffect reset(final Instrumentation inst, final Matcher classNameMatcher)
                throws UnmodifiableClassException {
    
            final EnhancerAffect affect = new EnhancerAffect();
            final Set<Class<?>> enhanceClassSet = findClassesToReset(classNameMatcher);
    
            try {
                enhance(inst, enhanceClassSet);
                logger.info("Success to reset classes: " + enhanceClassSet);
            } finally {
                for (Class<?> resetClass : enhanceClassSet) {
                    classBytesCache.remove(resetClass);
                    affect.cCnt(1);
                }
            }
    
            return affect;
        }

        private static Set<Class<?>> findClassesToReset(final Matcher classNameMatcher) {
            final Set<Class<?>> enhanceClassSet = new HashSet<Class<?>>();
            for (Class<?> classInCache : classBytesCache.keySet()) {
                if (classNameMatcher.matching(classInCache.getName())) {
                    enhanceClassSet.add(classInCache);
                }
            }
            return enhanceClassSet;
        }
}
