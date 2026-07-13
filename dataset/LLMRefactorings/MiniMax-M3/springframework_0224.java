public class springframework_0224 {

    	@Override
    	public void afterPropertiesSet() throws MBeanServerNotFoundException {
    		if (this.locateExistingServerIfPossible || this.agentId != null) {
    			try {
    				this.server = locateMBeanServer(this.agentId);
    			}
    			catch (MBeanServerNotFoundException ex) {
    				if (this.agentId != null) {
    					throw ex;
    				}
    				logger.debug("No existing MBeanServer found - creating new one");
    			}
    		}

    		createNewMBeanServerIfNeeded();
    	}

    	private void createNewMBeanServerIfNeeded() {
    		if (this.server == null) {
    			this.server = createMBeanServer(this.defaultDomain, this.registerWithFactory);
    			this.newlyRegistered = this.registerWithFactory;
    		}
    	}
}
