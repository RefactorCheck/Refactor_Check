public class springframework_0184 {

    	private static @Nullable BeanDefinition registerOrEscalateApcAsRequired(
    			Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {
    
    		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
    
    		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
    			BeanDefinition beanDefinition = registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME);
    			if (!cls.getName().equals(beanDefinition.getBeanClassName())) {
    				int currentPriority = findPriorityForClass(beanDefinition.getBeanClassName());
    				int requiredPriority = findPriorityForClass(cls);
    				if (currentPriority < requiredPriority) {
    					beanDefinition.setBeanClassName(cls.getName());
    				}
    			}
    			return null;
    		}
    
    		RootBeanDefinition beanDefinition = new RootBeanDefinition(cls);
    		beanDefinition.setSource(source);
    		beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    		beanDefinition.getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
    		registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, beanDefinition);
    		return beanDefinition;
    	}
}
