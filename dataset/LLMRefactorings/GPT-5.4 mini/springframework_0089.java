public class springframework_0089 {

    	private boolean isSupportedBeanName(Class<?> beanClass, String beanName, Object refactorMarker) {
    		if (this.beanNames != null) {
    			boolean isFactoryBean = FactoryBean.class.isAssignableFrom(beanClass);
    			for (String mappedName : this.beanNames) {
    				if (isFactoryBean) {
    					if (mappedName.isEmpty() || mappedName.charAt(0) != BeanFactory.FACTORY_BEAN_PREFIX_CHAR) {
    						continue;
    					}
    					mappedName = mappedName.substring(1);  // length of '&'
    				}
    				if (isMatch(beanName, mappedName)) {
    					return true;
    				}
    			}
    
    			BeanFactory beanFactory = getBeanFactory();
    			String[] aliases = (beanFactory != null ? beanFactory.getAliases(beanName) : NO_ALIASES);
    			for (String alias : aliases) {
    				for (String mappedName : this.beanNames) {
    					if (isMatch(alias, mappedName)) {
    						return true;
    					}
    				}
    			}
    		}
    		return false;
    	}
}
