public class springframework_0248 {

    	@Override
    	public Destination resolveDestinationName(@Nullable Session session, String destinationName, boolean pubSubDomain)
    			throws JMSException {
    
    		Assert.notNull(destinationName, "Destination name must not be null");
    		Destination dest = this.destinationCache.get(destinationName);
    		if (dest != null) {
    			validateDestination(dest, destinationName, pubSubDomain);
    		}
    		else {
    			dest = resolveJndiDestination(destinationName, session, pubSubDomain);
    			if (this.cache) {
    				this.destinationCache.put(destinationName, dest);
    			}
    		}
    		return dest;
    	}

    	private Destination resolveJndiDestination(String destinationName, Session session, boolean pubSubDomain)
    			throws JMSException {
    		try {
    			Destination dest = lookup(destinationName, Destination.class);
    			validateDestination(dest, destinationName, pubSubDomain);
    			return dest;
    		}
    		catch (NamingException ex) {
    			if (logger.isDebugEnabled()) {
    				logger.debug("Destination [" + destinationName + "] not found in JNDI", ex);
    			}
    			if (this.fallbackToDynamicDestination) {
    				return this.dynamicDestinationResolver.resolveDestinationName(session, destinationName, pubSubDomain);
    			}
    			else {
    				throw new DestinationResolutionException(
    						"Destination [" + destinationName + "] not found in JNDI", ex);
    			}
    		}
    	}
}
