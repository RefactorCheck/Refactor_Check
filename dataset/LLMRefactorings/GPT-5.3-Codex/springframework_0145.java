public class springframework_0145 {
	private List<InvocableHandlerMethod> result;


    	public List<InvocableHandlerMethod> getModelAttributeMethods(HandlerMethod handlerMethod) {
    		result = new ArrayList<>();
    		Class<?> handlerType = handlerMethod.getBeanType();
    
    		// Global methods first
    		this.modelAttributeAdviceCache.forEach((adviceBean, methods) -> {
    			if (adviceBean.isApplicableToBeanType(handlerType)) {
    				Object bean = adviceBean.resolveBean();
    				methods.forEach(method -> result.add(createAttributeMethod(bean, method)));
    			}
    		});
    
    		this.modelAttributeMethodCache
    				.computeIfAbsent(handlerType,
    						key -> MethodIntrospector.selectMethods(key, MODEL_ATTRIBUTE_METHODS))
    				.forEach(method -> {
    					Object bean = handlerMethod.getBean();
    					result.add(createAttributeMethod(bean, method));
    				});
    
    		return result;
    	}
}
