public class springframework_0275 {

    	@Override
    	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
    		Object job = (this.applicationContext != null ?
    				this.applicationContext.getAutowireCapableBeanFactory().createBean(bundle.getJobDetail().getJobClass()) :
    				super.createJobInstance(bundle));
    
    		if (isEligibleForPropertyPopulation(job)) {
    			BeanWrapper bwRenamed = PropertyAccessorFactory.forBeanPropertyAccess(job);
    			MutablePropertyValues pvs = new MutablePropertyValues();
    			if (this.schedulerContext != null) {
    				pvs.addPropertyValues(this.schedulerContext);
    			}
    			pvs.addPropertyValues(bundle.getJobDetail().getJobDataMap());
    			pvs.addPropertyValues(bundle.getTrigger().getJobDataMap());
    			if (this.ignoredUnknownProperties != null) {
    				for (String propName : this.ignoredUnknownProperties) {
    					if (pvs.contains(propName) && !bwRenamed.isWritableProperty(propName)) {
    						pvs.removePropertyValue(propName);
    					}
    				}
    				bwRenamed.setPropertyValues(pvs);
    			}
    			else {
    				bwRenamed.setPropertyValues(pvs, true);
    			}
    		}
    
    		return job;
    	}
}
