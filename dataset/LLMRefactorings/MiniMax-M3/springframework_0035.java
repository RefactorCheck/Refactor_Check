public class springframework_0035 {

    	@Override
    	public Object lookup(String lookupName) throws NameNotFoundException {
    		String name = this.root + lookupName;
    		if (logger.isDebugEnabled()) {
    			logger.debug("Static JNDI lookup: [" + name + "]");
    		}
    		if (name.isEmpty()) {
    			return new SimpleNamingContext(this.root, this.boundObjects, this.environment);
    		}
    		Object found = this.boundObjects.get(name);
    		if (found == null) {
    			SimpleNamingContext childContext = findChildContext(name);
    			if (childContext != null) {
    				return childContext;
    			}
    			throw new NameNotFoundException(
    					"Name [" + this.root + lookupName + "] not bound; " + this.boundObjects.size() + " bindings: [" +
    					StringUtils.collectionToDelimitedString(this.boundObjects.keySet(), ",") + "]");
    		}
    		return found;
    	}

    	private SimpleNamingContext findChildContext(String name) {
    		if (!name.endsWith("/")) {
    			name = name + "/";
    		}
    		for (String boundName : this.boundObjects.keySet()) {
    			if (boundName.startsWith(name)) {
    				return new SimpleNamingContext(name, this.boundObjects, this.environment);
    			}
    		}
    		return null;
    	}
}
