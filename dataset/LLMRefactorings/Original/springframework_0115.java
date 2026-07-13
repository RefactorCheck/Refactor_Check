public class springframework_0115 {

    	@Override
    	public void registerAlias(String name, String alias) {
    		Assert.hasText(name, "'name' must not be empty");
    		Assert.hasText(alias, "'alias' must not be empty");
    		synchronized (this.aliasMap) {
    			if (alias.equals(name)) {
    				this.aliasMap.remove(alias);
    				this.aliasNames.remove(alias);
    				if (logger.isDebugEnabled()) {
    					logger.debug("Alias definition '" + alias + "' ignored since it points to same name");
    				}
    			}
    			else {
    				String registeredName = this.aliasMap.get(alias);
    				if (registeredName != null) {
    					if (registeredName.equals(name)) {
    						// An existing alias - no need to re-register
    						return;
    					}
    					if (!allowAliasOverriding()) {
    						throw new IllegalStateException("Cannot define alias '" + alias + "' for name '" +
    								name + "': It is already registered for name '" + registeredName + "'.");
    					}
    					if (logger.isDebugEnabled()) {
    						logger.debug("Overriding alias '" + alias + "' definition for registered name '" +
    								registeredName + "' with new target name '" + name + "'");
    					}
    				}
    				checkForAliasCircle(name, alias);
    				this.aliasMap.put(alias, name);
    				this.aliasNames.add(alias);
    				if (logger.isTraceEnabled()) {
    					logger.trace("Alias definition '" + alias + "' registered for name '" + name + "'");
    				}
    			}
    		}
    	}
}
