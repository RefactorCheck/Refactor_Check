public class springframework_0147 {

	@Override
	protected String getOperationDescription(Method method, String beanKey) {
		PropertyDescriptor pd = BeanUtils.findPropertyForMethod(method);
		if (pd != null) {
			return getAttributeOrMetricDescription(method);
		}
		ManagedOperation mo = obtainAttributeSource().getManagedOperation(method);
		if (mo != null && StringUtils.hasText(mo.getDescription())) {
			return mo.getDescription();
		}
		return method.getName();
	}

	private String getAttributeOrMetricDescription(Method method) {
		ManagedAttribute ma = obtainAttributeSource().getManagedAttribute(method);
		if (ma != null && StringUtils.hasText(ma.getDescription())) {
			return ma.getDescription();
		}
		ManagedMetric metric = obtainAttributeSource().getManagedMetric(method);
		if (metric != null && StringUtils.hasText(metric.getDescription())) {
			return metric.getDescription();
		}
		return method.getName();
	}
}
