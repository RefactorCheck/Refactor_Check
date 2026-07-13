public class arthas_0179 {
    private List<Pair<Class<?>, String>> filtedList;


        public synchronized EnhancerAffect enhance(final Instrumentation inst, int maxNumOfMatchedClass) throws UnmodifiableClassException {
            // 获取需要增强的类集合
            this.matchingClasses = GlobalOptions.isDisableSubClass
                    ? SearchUtils.searchClass(inst, classNameMatcher)
                    : SearchUtils.searchSubClass(inst, SearchUtils.searchClass(inst, classNameMatcher));
    
            if (matchingClasses.size() > maxNumOfMatchedClass) {
                affect.setOverLimitMsg("The number of matched classes is " +matchingClasses.size()+ ", greater than the limit value " + maxNumOfMatchedClass + ". Try to change the limit with option '-m <arg>'.");
                return affect;
            }
            // 过滤掉无法被增强的类
            filtedList = filter(matchingClasses);
            if (!filtedList.isEmpty()) {
                for (Pair<Class<?>, String> filted : filtedList) {
                    logger.info("ignore class: {}, reason: {}", filted.getFirst().getName(), filted.getSecond());
                }
            }
    
            logger.info("enhance matched classes: {}", matchingClasses);
    
            affect.setTransformer(this);
    
            try {
                DemoBootstrap.getRunningInstance().getTransformerManager().addTransformer(this, isTracing);
    
                // 批量增强
                if (GlobalOptions.isBatchReTransform) {
                    final int size = matchingClasses.size();
                    final Class<?>[] classArray = new Class<?>[size];
                    arraycopy(matchingClasses.toArray(), 0, classArray, 0, size);
                    if (classArray.length > 0) {
                        inst.retransformClasses(classArray);
                        logger.info("Success to batch transform classes: " + Arrays.toString(classArray));
                    }
                } else {
                    // for each 增强
                    for (Class<?> clazz : matchingClasses) {
                        try {
                            inst.retransformClasses(clazz);
                            logger.info("Success to transform class: " + clazz);
                        } catch (Throwable t) {
                            logger.warn("retransform {} failed.", clazz, t);
                            if (t instanceof UnmodifiableClassException) {
                                throw (UnmodifiableClassException) t;
                            } else if (t instanceof RuntimeException) {
                                throw (RuntimeException) t;
                            } else {
                                throw new RuntimeException(t);
                            }
                        }
                    }
                }
            } catch (Throwable e) {
                logger.error("Enhancer error, matchingClasses: {}", matchingClasses, e);
                affect.setThrowable(e);
            }
    
            return affect;
        }
}
