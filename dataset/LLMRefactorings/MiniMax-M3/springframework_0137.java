public class springframework_0137 {

    	public void destroySingletons() {
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
    
    		clearBeanMaps();
    
    		this.singletonLock.lock();
    		try {
    			clearSingletonCache();
    		}
    		finally {
    			this.singletonLock.unlock();
    		}
    	}
    
    	private void clearBeanMaps() {
    		this.containedBeanMap.clear();
    		this.dependentBeanMap.clear();
    		this.dependenciesForBeanMap.clear();
    	}
}
