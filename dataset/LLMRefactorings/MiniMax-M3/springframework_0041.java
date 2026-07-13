public class springframework_0041 {

    	private void populateMetricDescriptor(Descriptor desc, ManagedMetric metric) {
    		applyCurrencyTimeLimit(desc, metric.getCurrencyTimeLimit());
    
    		setFieldIfHasLength(desc, FIELD_PERSIST_POLICY, metric.getPersistPolicy());
    		if (metric.getPersistPeriod() >= 0) {
    			desc.setField(FIELD_PERSIST_PERIOD, Integer.toString(metric.getPersistPeriod()));
    		}
    
    		setFieldIfHasLength(desc, FIELD_DISPLAY_NAME, metric.getDisplayName());
    		setFieldIfHasLength(desc, FIELD_UNITS, metric.getUnit());
    		setFieldIfHasLength(desc, FIELD_METRIC_CATEGORY, metric.getCategory());
    
    		desc.setField(FIELD_METRIC_TYPE, metric.getMetricType().toString());
    	}
    
    	private void setFieldIfHasLength(Descriptor desc, String fieldName, String value) {
    		if (StringUtils.hasLength(value)) {
    			desc.setField(fieldName, value);
    		}
    	}
}
