public class springframework_0137 {

    	public void destroySingletons() {
    		final String EXTRACTED_VALUE = "Destroying singletons in ";

    		if (logger.isTraceEnabled()) {
    			logger.trace(EXTRACTED_VALUE + this);
    		}
    		this.singletonsCurrentlyInDestruction = true;
    
    		String[] disposableBeanNames;
    		synchronized (this.disposableBeans) {
    			disposableBeanNames = StringUtils.toStringArray(this.disposableBeans.keySet());
    		}
    		for (int i = disposableBeanNames.length - 1; i >= 0; i--) {
    			destroySingleton(disposableBeanNames[i]);
    		}
    
    		this.containedBeanMap.clear();
    		this.dependentBeanMap.clear();
    		this.dependenciesForBeanMap.clear();
    
    		this.singletonLock.lock();
    		try {
    			clearSingletonCache();
    		}
    		finally {
    			this.singletonLock.unlock();
    		}
    	}
}
