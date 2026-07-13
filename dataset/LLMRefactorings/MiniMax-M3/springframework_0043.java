public class springframework_0043 {

	protected @Nullable Object getSingleton(String beanName, boolean allowEarlyReference) {
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null && isSingletonCurrentlyInCreation(beanName)) {
			singletonObject = this.earlySingletonObjects.get(beanName);
			if (singletonObject == null && allowEarlyReference) {
				if (!this.singletonLock.tryLock()) {
					return null;
				}
				try {
					singletonObject = getSingletonUnderLock(beanName);
				}
				finally {
					this.singletonLock.unlock();
				}
			}
		}
		return singletonObject;
	}

	private @Nullable Object getSingletonUnderLock(String beanName) {
		Object singletonObject = this.singletonObjects.get(beanName);
		if (singletonObject == null) {
			singletonObject = this.earlySingletonObjects.get(beanName);
			if (singletonObject == null) {
				ObjectFactory<?> singletonFactory = this.singletonFactories.get(beanName);
				if (singletonFactory != null) {
					singletonObject = singletonFactory.getObject();
					if (this.singletonFactories.remove(beanName) != null) {
						this.earlySingletonObjects.put(beanName, singletonObject);
					}
					else {
						singletonObject = this.singletonObjects.get(beanName);
					}
				}
			}
		}
		return singletonObject;
	}
}
