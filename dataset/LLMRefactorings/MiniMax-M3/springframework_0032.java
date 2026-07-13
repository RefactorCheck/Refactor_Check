public class springframework_0032 {

    private static void registerCacheAdvisor(Element element, ParserContext parserContext) {
        if (!parserContext.getRegistry().containsBeanDefinition(CacheManagementConfigUtils.JCACHE_ADVISOR_BEAN_NAME)) {
            Object source = parserContext.extractSource(element);

            String sourceName = registerCacheOperationSource(element, parserContext, source);
            String interceptorName = registerCacheInterceptor(element, parserContext, source, sourceName);
            registerCacheAdvisorDefinition(element, parserContext, source, sourceName, interceptorName);
            registerCompositeComponent(element, parserContext, source, sourceName, interceptorName);
        }
    }

    private static String registerCacheOperationSource(Element element, ParserContext parserContext, Object source) {
        BeanDefinition sourceDef = createJCacheOperationSourceBeanDefinition(element, source);
        return parserContext.getReaderContext().registerWithGeneratedName(sourceDef);
    }

    private static String registerCacheInterceptor(Element element, ParserContext parserContext, Object source, String sourceName) {
        RootBeanDefinition interceptorDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.JCacheInterceptor");
        interceptorDef.setSource(source);
        interceptorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        interceptorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
        parseErrorHandler(element, interceptorDef);
        return parserContext.getReaderContext().registerWithGeneratedName(interceptorDef);
    }

    private static void registerCacheAdvisorDefinition(Element element, ParserContext parserContext, Object source, String sourceName, String interceptorName) {
        RootBeanDefinition advisorDef = new RootBeanDefinition("org.springframework.cache.jcache.interceptor.BeanFactoryJCacheOperationSourceAdvisor");
        advisorDef.setSource(source);
        advisorDef.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
        advisorDef.getPropertyValues().add("cacheOperationSource", new RuntimeBeanReference(sourceName));
        advisorDef.getPropertyValues().add("adviceBeanName", interceptorName);
        if (element.hasAttribute("order")) {
            advisorDef.getPropertyValues().add("order", element.getAttribute("order"));
        }
        parserContext.getRegistry().registerBeanDefinition(CacheManagementConfigUtils.JCACHE_ADVISOR_BEAN_NAME, advisorDef);
    }

    private static void registerCompositeComponent(Element element, ParserContext parserContext, Object source, String sourceName, String interceptorName) {
        CompositeComponentDefinition compositeDef = new CompositeComponentDefinition(element.getTagName(), source);
        compositeDef.addNestedComponent(new BeanComponentDefinition(sourceDef, sourceName));
        compositeDef.addNestedComponent(new BeanComponentDefinition(interceptorDef, interceptorName));
        compositeDef.addNestedComponent(new BeanComponentDefinition(advisorDef, CacheManagementConfigUtils.JCACHE_ADVISOR_BEAN_NAME));
        parserContext.registerComponent(compositeDef);
    }
}
