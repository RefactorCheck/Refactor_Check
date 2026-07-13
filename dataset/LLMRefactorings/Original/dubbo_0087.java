public class dubbo_0087 {

        public static int scanBasePackages(BeanDefinitionRegistry registry, String... basePackages) {
    
            int count = 0;
    
            if (!ObjectUtils.isEmpty(basePackages)) {
    
                boolean debugEnabled = logger.isDebugEnabled();
    
                if (debugEnabled) {
                    logger.debug(registry.getClass().getSimpleName() + " will scan base packages "
                            + Arrays.asList(basePackages) + ".");
                }
    
                List<String> registeredBeanNames = Arrays.asList(registry.getBeanDefinitionNames());
    
                ClassPathBeanDefinitionScanner classPathBeanDefinitionScanner =
                        new ClassPathBeanDefinitionScanner(registry);
                count = classPathBeanDefinitionScanner.scan(basePackages);
    
                List<String> scannedBeanNames = new ArrayList<String>(count);
                scannedBeanNames.addAll(Arrays.asList(registry.getBeanDefinitionNames()));
                scannedBeanNames.removeAll(registeredBeanNames);
    
                if (debugEnabled) {
                    logger.debug("The Scanned Components[ count : " + count + "] under base packages "
                            + Arrays.asList(basePackages) + " : ");
                }
    
                for (String scannedBeanName : scannedBeanNames) {
                    BeanDefinition scannedBeanDefinition = registry.getBeanDefinition(scannedBeanName);
                    if (debugEnabled) {
                        logger.debug("Component [ name : " + scannedBeanName + " , class : "
                                + scannedBeanDefinition.getBeanClassName() + " ]");
                    }
                }
            }
    
            return count;
        }
}
