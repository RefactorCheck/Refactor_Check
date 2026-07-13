public class springframework_0043 {

    	protected @Nullable Object getSingleton(String beanName, boolean allowEarlyReference) {
    		// Quick check for existing instance without full singleton lock.
    		Object singletonObject = this.singletonObjects.get(beanName);
    		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
    			singletonObject = this.earlySingletonObjects.get(beanName);
    			if (singletonObject == null && allowEarlyReference) {
    				if (!this.singletonLock.tryLock()) {
    					// Avoid early singleton inference outside of original creation thread.
    					return null;
    				}
    				try {
    					// Consistent creation of early reference within full singleton lock.
    					singletonObject = this.singletonObjects.get(beanName);
    					if (singletonObject == null) {
    						singletonObject = this.earlySingletonObjects.get(beanName);
    						if (singletonObject == null) {
    							ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
    							if (singletonFactory != null) {
    								singletonObject = singletonFactory.getObject();
    								// Singleton could have been added or removed in the meantime.
    								if (this.singletonFactories.remove(beanName) != null) {
    									this.earlySingletonObjects.put(beanName, singletonObject);
    								}
    								else {
    									singletonObject = this.singletonObjects.get(beanName);
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
    		return singletonObject;
    	}
}
