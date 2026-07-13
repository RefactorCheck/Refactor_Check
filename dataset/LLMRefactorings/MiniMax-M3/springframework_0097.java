public class springframework_0097 {

    private static @Nullable BeanDefinition createDatabasePopulator(Element element, List<Element> scripts, String execution) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CompositeDatabasePopulator.class);

        boolean ignoreFailedDrops = element.getAttribute("ignore-failures").equals("DROPS");
        boolean continueOnError = element.getAttribute("ignore-failures").equals("ALL");

        ManagedList<BeanMetadataElement> delegates = new ManagedList<>();
        for (Element scriptElement : scripts) {
            BeanDefinition delegate = createScriptPopulator(element, scriptElement, execution, ignoreFailedDrops, continueOnError);
            if (delegate != null) {
                delegates.add(delegate);
            }
        }

        if (delegates.isEmpty()) {
            return null;
        }

        builder.addPropertyValue("populators", delegates);
        return builder.getBeanDefinition();
    }

    private static @Nullable BeanDefinition createScriptPopulator(Element element, Element scriptElement, String execution, boolean ignoreFailedDrops, boolean continueOnError) {
        String executionAttr = scriptElement.getAttribute("execution");
        if (!StringUtils.hasText(executionAttr)) {
            executionAttr = "INIT";
        }
        if (!execution.equals(executionAttr)) {
            return null;
        }
        BeanDefinitionBuilder delegate = BeanDefinitionBuilder.genericBeanDefinition(ResourceDatabasePopulator.class);
        delegate.addPropertyValue("ignoreFailedDrops", ignoreFailedDrops);
        delegate.addPropertyValue("continueOnError", continueOnError);

        BeanDefinitionBuilder resourcesFactory = BeanDefinitionBuilder.genericBeanDefinition(SortedResourcesFactoryBean.class);
        resourcesFactory.addConstructorArgValue(new TypedStringValue(scriptElement.getAttribute("location")));
        delegate.addPropertyValue("scripts", resourcesFactory.getBeanDefinition());
        if (StringUtils.hasLength(scriptElement.getAttribute("encoding"))) {
            delegate.addPropertyValue("sqlScriptEncoding", new TypedStringValue(scriptElement.getAttribute("encoding")));
        }
        String separator = getSeparator(element, scriptElement);
        if (separator != null) {
            delegate.addPropertyValue("separator", new TypedStringValue(separator));
        }
        return delegate.getBeanDefinition();
    }
}
