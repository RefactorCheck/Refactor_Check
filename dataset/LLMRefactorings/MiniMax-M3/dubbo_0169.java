public class dubbo_0169 {

        public static Set<BeanDefinitionHolder> findBeanDefinitionHolders(
                ClassPathBeanDefinitionScanner scanner,
                String packageToScan,
                BeanDefinitionRegistry registry,
                BeanNameGenerator beanNameGenerator) {
    
            Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(packageToScan);
    
            Set<BeanDefinitionHolder> beanDefinitionHolders =
                    new LinkedHashSet<BeanDefinitionHolder>(beanDefinitions.size());
    
            for (BeanDefinition beanDefinition : beanDefinitions) {
                beanDefinitionHolders.add(createBeanDefinitionHolder(beanDefinition, registry, beanNameGenerator));
            }
    
            return beanDefinitionHolders;
        }

        private static BeanDefinitionHolder createBeanDefinitionHolder(
                BeanDefinition beanDefinition,
                BeanDefinitionRegistry registry,
                BeanNameGenerator beanNameGenerator) {
            String beanName = beanNameGenerator.generateBeanName(beanDefinition, registry);
            return new BeanDefinitionHolder(beanDefinition, beanName);
        }
}
