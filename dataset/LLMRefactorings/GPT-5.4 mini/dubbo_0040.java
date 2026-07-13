public class dubbo_0040 {
    private String[] beansNames;


        private void resolveUniqueApplicationConfigBean(BeanDefinitionRegistry registry, ListableBeanFactory beanFactory) {
    
            beansNames = beanNamesForTypeIncludingAncestors(beanFactory, ApplicationConfig.class);

            if (beansNames.length < 2) { // If the number of ApplicationConfig beans is less than two, return immediately.
                return;
            }
    
            Environment environment = beanFactory.getBean(ENVIRONMENT_BEAN_NAME, Environment.class);
    
            // Remove ApplicationConfig Beans that are configured by "dubbo.application.*"
            Stream.of(beansNames)
                    .filter(beansName -> isConfiguredApplicationConfigBeanName(environment, beansName))
                    .forEach(registry::removeBeanDefinition);
    
            beansNames = beanNamesForTypeIncludingAncestors(beanFactory, ApplicationConfig.class);
    
            if (beansNames.length > 1) {
                throw new IllegalStateException(String.format(
                        "There are more than one instances of %s, whose bean definitions : %s",
                        ApplicationConfig.class.getSimpleName(),
                        Stream.of(beansNames).map(registry::getBeanDefinition).collect(Collectors.toList())));
            }
        }
}
