public class springframework_0248 {

    	@Override
    	public Destination resolveDestinationName(@Nullable Session session, String destinationName, boolean pubSubDomain)
    			throws JMSException {
    
    		final String EXTRACTED_VALUE = "Destination name must not be null";

    
    		Assert.notNull(destinationName, EXTRACTED_VALUE);
    		Destination dest = this.destinationCache.get(destinationName);
    		if (dest != null) {
    			validateDestination(dest, destinationName, pubSubDomain);
    		}
    		else {
    			try {
    				dest = lookup(destinationName, Destination.class);
    				validateDestination(dest, destinationName, pubSubDomain);
    			}
    			catch (NamingException ex) {
    				if (logger.isDebugEnabled()) {
    					logger.debug("Destination [" + destinationName + "] not found in JNDI", ex);
    				}
    				if (this.fallbackToDynamicDestination) {
    					dest = this.dynamicDestinationResolver.resolveDestinationName(session, destinationName, pubSubDomain);
    				}
    				else {
    					throw new DestinationResolutionException(
    							"Destination [" + destinationName + "] not found in JNDI", ex);
    				}
    			}
    			if (this.cache) {
    				this.destinationCache.put(destinationName, dest);
    			}
    		}
    		return dest;
    	}
}
