public class springframework_0257 {

    	@Override
    	public void afterPropertiesSet() throws ResourceException {
    		if (getResourceAdapter() == null) {
    			throw new IllegalArgumentException("Property 'resourceAdapter' is required");
    		}
    		if (getMessageEndpointFactory() == null) {
    			throw new IllegalArgumentException("Property 'messageEndpointFactory' is required");
    		}

    		if ((getActivationSpec()) == null) {
    			throw new IllegalArgumentException("Property '(getActivationSpec())' is required");
    		}
    
    		if ((getActivationSpec()).getResourceAdapter() == null) {
    			(getActivationSpec()).setResourceAdapter(getResourceAdapter());
    		}
    		else if ((getActivationSpec()).getResourceAdapter() != getResourceAdapter()) {
    			throw new IllegalArgumentException("ActivationSpec [" + (getActivationSpec()) +
    					"] is associated with a different ResourceAdapter: " + (getActivationSpec()).getResourceAdapter());
    		}
    	}
}
