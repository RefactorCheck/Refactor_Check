public class springframework_0003 {
    private static final String EXTRACTED_CONSTANT = "Could not process key '";


    	@Override
    	protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props)
    			throws BeansException {
    
    		for (Enumeration<?> names = props.propertyNames(); names.hasMoreElements();) {
    			String key = (String) names.nextElement();
    			try {
    				processKey(beanFactory, key, props.getProperty(key));
    			}
    			catch (BeansException ex) {
    				String msg = EXTRACTED_CONSTANT + key + "' in PropertyOverrideConfigurer";
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
