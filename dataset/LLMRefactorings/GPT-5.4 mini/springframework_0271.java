public class springframework_0271 {

    	@Override
    	public void afterPropertiesSet() throws MBeanServerNotFoundException, MBeanInfoRetrievalException {
    		String extractedValue = "Property 'proxyInterface' or 'managementInterface' is required";
    		super.afterPropertiesSet();
    
    		Class<?> interfaceToUse;
    		if (this.proxyInterface == null) {
    			interfaceToUse = getManagementInterface();
    			if (interfaceToUse == null) {
    				throw new IllegalArgumentException(extractedValue);
    			}
    			this.proxyInterface = interfaceToUse;
    		}
    		else {
    			interfaceToUse = this.proxyInterface;
    			if (getManagementInterface() == null) {
    				setManagementInterface(interfaceToUse);
    			}
    		}
    		this.mbeanProxy = new ProxyFactory(interfaceToUse, this).getProxy(this.beanClassLoader);
    	}
}
