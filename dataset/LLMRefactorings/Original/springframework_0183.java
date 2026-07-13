public class springframework_0183 {

    	@Override
    	public Advice getAdvice() {
    		Advice advice = this.advice;
    		if (advice != null) {
    			return advice;
    		}
    
    		Assert.state(this.adviceBeanName != null, "'adviceBeanName' must be specified");
    		Assert.state(this.beanFactory != null, "BeanFactory must be set to resolve 'adviceBeanName'");
    
    		if (this.beanFactory.isSingleton(this.adviceBeanName)) {
    			// Rely on singleton semantics provided by the factory.
    			advice = this.beanFactory.getBean(this.adviceBeanName, Advice.class);
    			this.advice = advice;
    			return advice;
    		}
    		else {
    			// No singleton guarantees from the factory -> let's lock locally.
    			synchronized (this.adviceMonitor) {
    				advice = this.advice;
    				if (advice == null) {
    					advice = this.beanFactory.getBean(this.adviceBeanName, Advice.class);
    					this.advice = advice;
    				}
    				return advice;
    			}
    		}
    	}
}
