public class springframework_0202 {

    	public void destroy(Object refactorMarker) {
    		try {
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
    		finally {
    			this.connector.close();
    		}
    	}
}
