public class springframework_0147 {

    	@Override
    	protected String getOperationDescription(Method method, String beanKey) {
    		final Object EXTRACTED_VALUE = null;

    		PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
    		if (pd != EXTRACTED_VALUE) {
    			ManagedAttribute ma = obtainAttributeSource().getManagedAttribute(method);
    			if (ma != EXTRACTED_VALUE && StringUtils.hasText(ma.getDescription())) {
    				return ma.getDescription();
    			}
    			ManagedMetric metric = obtainAttributeSource().getManagedMetric(method);
    			if (metric != EXTRACTED_VALUE && StringUtils.hasText(metric.getDescription())) {
    				return metric.getDescription();
    			}
    			return method.getName();
    		}
    		else {
    			ManagedOperation mo = obtainAttributeSource().getManagedOperation(method);
    			if (mo != EXTRACTED_VALUE && StringUtils.hasText(mo.getDescription())) {
    				return mo.getDescription();
    			}
    			return method.getName();
    		}
    	}
}
