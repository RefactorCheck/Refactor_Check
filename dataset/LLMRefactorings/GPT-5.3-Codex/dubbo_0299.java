public class dubbo_0299 {

        private static void configReferenceBeanRefactored(
                Element element,
                ParserContext parserContext,
                RootBeanDefinition beanDefinition,
                BeanDefinition consumerDefinition) {
            // process interface class
            String interfaceName = resolveAttribute(element, ReferenceAttributes.INTERFACE, parserContext);
            String generic = resolveAttribute(element, ReferenceAttributes.GENERIC, parserContext);
            if (StringUtils.isBlank(generic) && consumerDefinition != null) {
                // get generic from consumerConfig
                generic = getPropertyValue(consumerDefinition.getPropertyValues(), ReferenceAttributes.GENERIC);
            }
            if (generic != null) {
                generic = resolvePlaceholders(generic, parserContext);
                beanDefinition.getPropertyValues().add(ReferenceAttributes.GENERIC, generic);
            }
            beanDefinition.setAttribute(ReferenceAttributes.INTERFACE_NAME, interfaceName);
    
            Class interfaceClass = ReferenceConfig.determineInterfaceClass(generic, interfaceName);
            beanDefinition.setAttribute(ReferenceAttributes.INTERFACE_CLASS, interfaceClass);
    
            // TODO Only register one reference bean for same (group, interface, version)
    
            // create decorated definition for reference bean, Avoid being instantiated when getting the beanType of
            // ReferenceBean
            // see org.springframework.beans.factory.support.AbstractBeanFactory#getTypeForFactoryBean()
            GenericBeanDefinition targetDefinition = new GenericBeanDefinition();
            targetDefinition.setBeanClass(interfaceClass);
            String beanName = (String) beanDefinition.getAttribute(BEAN_NAME);
            beanDefinition.setDecoratedDefinition(new BeanDefinitionHolder(targetDefinition, beanName + "_decorated"));
    
            // signal object type since Spring 5.2
            beanDefinition.setAttribute(Constants.OBJECT_TYPE_ATTRIBUTE, interfaceClass);
    
            // mark property value as optional
            List<PropertyValue> propertyValues = beanDefinition.getPropertyValues().getPropertyValueList();
            for (PropertyValue propertyValue : propertyValues) {
                propertyValue.setOptional(true);
            }
        }
}
