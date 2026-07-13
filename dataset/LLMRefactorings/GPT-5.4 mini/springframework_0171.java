public class springframework_0171 {

    	public static <T> ObjectProvider<T> getBeanProvider(Class<T> requiredType) {
    		return new ObjectProvider<>() {
    			@Override
    			public T getObject() throws BeansException {
    				return getBean(requiredType);
    			}
    			@Override
    			public T getObject(@Nullable Object... args) throws BeansException {
    				return getBean(requiredType, args);
    			}
    			@Override
    			public @Nullable T getIfAvailable() throws BeansException {
    				try {
    					return getBean(requiredType);
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
    					return getBean(requiredType);
    				}
    				catch (NoSuchBeanDefinitionException ex) {
    					return null;
    				}
    			}
    		};
    	}
}
