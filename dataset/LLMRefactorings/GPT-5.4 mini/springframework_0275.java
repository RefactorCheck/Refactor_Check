public class springframework_0275 {

    	@Override
    	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    		Object job = (this.applicationContext != null ?
    				this.applicationContext.getAutowireCapableBeanFactory().createBean(bundle.getJobDetail().getJobClass()) :
    				super.createJobInstance(bundle));
    
    		if (isEligibleForPropertyPopulation(job)) {
    			MutablePropertyValues pvs = new MutablePropertyValues();
    			if (this.schedulerContext != null) {
    				pvs.addPropertyValues(this.schedulerContext);
    			}
    			pvs.addPropertyValues(bundle.getJobDetail().getJobDataMap());
    			pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());
    			if (this.ignoredUnknownProperties != null) {
    				for (String propName : this.ignoredUnknownProperties) {
    					if (pvs.contains(propName) && !(PropertyAccessorFactory.forBeanPropertyAccess(job)).isWritableProperty(propName)) {
    						pvs.removePropertyValue(propName);
    					}
    				}
    				(PropertyAccessorFactory.forBeanPropertyAccess(job)).setPropertyValues(pvs);
    			}
    			else {
    				(PropertyAccessorFactory.forBeanPropertyAccess(job)).setPropertyValues(pvs, true);
    			}
    		}
    
    		return job;
    	}
}
