public class springframework_0271 {

    	@Override
    	public void afterPropertiesSet() throws MBeanServerNotFoundException, MBeanInfoRetrievalException {
    		applyExtractedRefactoring();

    		Class<?> interfaceToUse;
    		if (this.proxyInterface == null) {
    			interfaceToUse = getManagementInterface();
    			if (interfaceToUse == null) {
    				throw new IllegalArgumentException("Property 'proxyInterface' or 'managementInterface' is required");
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

	private void applyExtractedRefactoring() {
    		super.afterPropertiesSet();
	}
}
