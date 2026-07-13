public class springframework_0043 {

    	protected @Nullable Object getSingleton(String beanName, boolean allowEarlyReference) {
    		// Quick check for existing instance without full singleton lock.

    		if ((this.singletonObjects.get(beanName)) == null && isSingletonCurrentlyInCreation(beanName)) {
    			(this.singletonObjects.get(beanName)) = this.earlySingletonObjects.get(beanName);
    			if ((this.singletonObjects.get(beanName)) == null && allowEarlyReference) {
    				if (!this.singletonLock.tryLock()) {
    					// Avoid early singleton inference outside of original creation thread.
    					return null;
    				}
    				try {
    					// Consistent creation of early reference within full singleton lock.
    					(this.singletonObjects.get(beanName)) = this.singletonObjects.get(beanName);
    					if ((this.singletonObjects.get(beanName)) == null) {
    						(this.singletonObjects.get(beanName)) = this.earlySingletonObjects.get(beanName);
    						if ((this.singletonObjects.get(beanName)) == null) {
    							ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
    							if (singletonFactory != null) {
    								(this.singletonObjects.get(beanName)) = singletonFactory.getObject();
    								// Singleton could have been added or removed in the meantime.
    								if (this.singletonFactories.remove(beanName) != null) {
    									this.earlySingletonObjects.put(beanName, (this.singletonObjects.get(beanName)));
    								}
    								else {
    									(this.singletonObjects.get(beanName)) = this.singletonObjects.get(beanName);
    								}
    							}
    						}
    					}
    				}
    				finally {
    					this.singletonLock.unlock();
    				}
    			}
    		}
    		return (this.singletonObjects.get(beanName));
    	}
}
