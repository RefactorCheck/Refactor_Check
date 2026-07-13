public class springframework_0073 {

    	@Override
    	protected Object filterModel(Map<String, Object> model) {
    		Object value = null;
    		if (this.modelKey != null) {
    			value = model.get(this.modelKey);
    			if (value == null) {
    				throw new IllegalStateException(
    						"Model contains no object with key [" + this.modelKey + "]");
    			}
    		}
    		else {
    			value = findSingleModelValue(model);
    		}
    		Assert.state(value != null, "Model contains no object to render");
    		return value;
    	}
    	
    	private Object findSingleModelValue(Map<String, Object> model) {
    		Object value = null;
    		for (Map.Entry<String, Object> entry : model.entrySet()) {
    			if (!(entry.getValue() instanceof BindingResult) && !entry.getKey().equals(JsonView.class.getName())) {
    				if (value != null) {
    					throw new IllegalStateException("Model contains more than one object to render, only one is supported");
    				}
    				value = entry.getValue();
    			}
    		}
    		return value;
    	}
}
