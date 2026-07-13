public class dubbo_0169 {

        public static Set<BeanDefinitionHolder> findBeanDefinitionHoldersRefactored(
                ClassPathBeanDefinitionScanner scanner,
                String packageToScan,
                BeanDefinitionRegistry registry,
                BeanNameGenerator beanNameGenerator) {
    
            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);
    
            Set<BeanDefinitionHolder> beanDefinitionHolders =
                    new LinkedHashSet<BeanDefinitionHolder>(beanDefinitions.size());
    
            for (BeanDefinition beanDefinition : beanDefinitions) {
    
                String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
                BeanDefinitionHolder beanDefinitionHolder = new BeanDefinitionHolder(beanDefinition, beanName);
                beanDefinitionHolders.add(beanDefinitionHolder);
            }
    
            return beanDefinitionHolders;
        }
}
