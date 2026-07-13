public class springframework_0171 {

    	@Override
    	public <T> ObjectProvider<T> getBeanProvider(Class<T> requiredTypeValue) {
    		return new ObjectProvider<>() {
    			@Override
    			public T getObject() throws BeansException {
    				return getBean(requiredTypeValue);
    			}
    			@Override
    			public T getObject(@Nullable Object... args) throws BeansException {
    				return getBean(requiredTypeValue, args);
    			}
    			@Override
    			public @Nullable T getIfAvailable() throws BeansException {
    				try {
    					return getBean(requiredTypeValue);
    				}
    				catch (NoUniqueBeanDefinitionException ex) {
    					throw ex;
    				}
    				catch (NoSuchBeanDefinitionException ex) {
    					return null;
    				}
    			}
    			@Override
    			public @Nullable T getIfUnique() throws BeansException {
    				try {
    					return getBean(requiredTypeValue);
    				}
    				catch (NoSuchBeanDefinitionException ex) {
    					return null;
    				}
    			}
    		};
    	}
}
