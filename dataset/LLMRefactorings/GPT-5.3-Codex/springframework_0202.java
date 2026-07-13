public class springframework_0202 {

    	@Override
    	public void destroy() {
    		try {
    			if (this.server != null && this.actualObjectNames != null) {
    				for (ObjectName actualObjectName : this.actualObjectNames) {
    					try {
    						this.server.removeNotificationListener(
    								applyExtractedRefactoring();

    					}
    					catch (Exception ex) {
    						if (logger.isDebugEnabled()) {
    							logger.debug("Unable to unregister NotificationListener", ex);
    						}
    					}
    				}
    			}
    		}
    		finally {
    			this.connector.close();
    		}
    	}

	private void applyExtractedRefactoring() {
    								actualObjectName, getNotificationListener(), getNotificationFilter(), getHandback());
	}
}
