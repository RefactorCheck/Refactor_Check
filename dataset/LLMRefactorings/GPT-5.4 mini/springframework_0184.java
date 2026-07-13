public class springframework_0184 {

    	private static @Nullable BeanDefinition registerOrEscalateApcAsRequired(
    			Class<?> cls, BeanDefinitionRegistry registry, @Nullable Object source) {
    
    		Assert.notNull(registry, "BeanDefinitionRegistry must not be null");
    
    		if (registry.containsBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) {
    			if (!cls.getName().equals((registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)).getBeanClassName())) {
    				int currentPriority = findPriorityForClass((registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)).getBeanClassName());
    				int requiredPriority = findPriorityForClass(cls);
    				if (currentPriority < requiredPriority) {
    					(registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)).setBeanClassName(cls.getName());
    				}
    			}
    			return null;
    		}
    
    		RootBeanDefinition (registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)) = new RootBeanDefinition(cls);
    		(registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)).setSource(source);
    		(registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)).setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
    		(registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)).getPropertyValues().add("order", Ordered.HIGHEST_PRECEDENCE);
    		registry.registerBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME, (registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME)));
    		return (registry.getBeanDefinition(AUTO_PROXY_CREATOR_BEAN_NAME));
    	}
}
