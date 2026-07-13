public class springframework_0003 {

    	@Override
    	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
    			throws BeansException {
    
    		final String EXTRACTED_VALUE = "Could not process key '";

    
    		for (Enumeration<?> names = props.propertyNames(); names.hasMoreElements();) {
    			String key = (String) names.nextElement();
    			try {
    				processKey(beanFactory, key, props.getProperty(key));
    			}
    			catch (BeansException ex) {
    				String msg = EXTRACTED_VALUE + key + "' in PropertyOverrideConfigurer";
    				if (!this.ignoreInvalidKeys) {
    					throw new BeanInitializationException(msg, ex);
    				}
    				if (logger.isDebugEnabled()) {
    					logger.debug(msg, ex);
    				}
    			}
    		}
    	}
}
