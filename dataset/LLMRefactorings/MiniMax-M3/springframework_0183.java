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
			advice = createAdvice();
			this.advice = advice;
			return advice;
		}
		else {
			synchronized (this.adviceMonitor) {
				advice = this.advice;
				if (advice == null) {
					advice = createAdvice();
					this.advice = advice;
				}
				return advice;
			}
		}
	}

	private Advice createAdvice() {
		return this.beanFactory.getBean(this.adviceBeanName, Advice.class);
	}
}
