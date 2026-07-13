public class springframework_0202 {

    	@Override
    	public void destroy() {
    		try {
    			removeNotificationListeners();
    		}
    		finally {
    			this.connector.close();
    		}
    	}

    	private void removeNotificationListeners() {
    		if (this.server != null && this.actualObjectNames != null) {
    			for (ObjectName actualObjectName : this.actualObjectNames) {
    				try {
    					this.server.removeNotificationListener(
    							actualObjectName, getNotificationListener(), getNotificationFilter(), getHandback());
    				}
    				catch (Exception ex) {
    					if (logger.isDebugEnabled()) {
    						logger.debug("Unable to unregister NotificationListener", ex);
    					}
    				}
    			}
    		}
    	}
}
