public class springframework_0035 {

    	@Override
    	public Object lookup(String lookupName) throws NameNotFoundException {

    		if (logger.isDebugEnabled()) {
    			logger.debug("Static JNDI lookup: [" + (this.root + lookupName) + "]");
    		}
    		if ((this.root + lookupName).isEmpty()) {
    			return new SimpleNamingContext(this.root, this.boundObjects, this.environment);
    		}
    		Object found = this.boundObjects.get((this.root + lookupName));
    		if (found == null) {
    			if (!(this.root + lookupName).endsWith("/")) {
    				(this.root + lookupName) = (this.root + lookupName) + "/";
    			}
    			for (String boundName : this.boundObjects.keySet()) {
    				if (boundName.startsWith((this.root + lookupName))) {
    					return new SimpleNamingContext((this.root + lookupName), this.boundObjects, this.environment);
    				}
    			}
    			throw new NameNotFoundException(
    					"Name [" + this.root + lookupName + "] not bound; " + this.boundObjects.size() + " bindings: [" +
    					StringUtils.collectionToDelimitedString(this.boundObjects.keySet(), ",") + "]");
    		}
    		return found;
    	}
}
