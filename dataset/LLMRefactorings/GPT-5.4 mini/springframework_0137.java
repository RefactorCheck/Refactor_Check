public class springframework_0137 {

    	public void destroySingletons() {
    		destroySingletonsExtracted();
    	}

    	private void destroySingletonsExtracted() {
    		if (logger.isTraceEnabled()) {
    			logger.trace("Destroying singletons in " + this);
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
