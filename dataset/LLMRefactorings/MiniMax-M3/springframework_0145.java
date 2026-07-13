public class springframework_0145 {

    	public List<InvocableHandlerMethod> getModelAttributeMethods(HandlerMethod handlerMethod) {
    		List<InvocableHandlerMethod> result = new ArrayList<>();
    		Class<?> handlerType = handlerMethod.getBeanType();
    
    		addGlobalModelAttributeMethods(result, handlerType);
    		addHandlerSpecificModelAttributeMethods(result, handlerMethod, handlerType);
    
    		return result;
    	}
    
    	private void addGlobalModelAttributeMethods(List<InvocableHandlerMethod> result, Class<?> handlerType) {
    		this.modelAttributeAdviceCache.forEach((adviceBean, methods) -> {
    			if (adviceBean.isApplicableToBeanType(handlerType)) {
    				Object bean = adviceBean.resolveBean();
    				methods.forEach(method -> result.add(createAttributeMethod(bean, method)));
    			}
    		});
    	}
    
    	private void addHandlerSpecificModelAttributeMethods(List<InvocableHandlerMethod> result, HandlerMethod handlerMethod, Class<?> handlerType) {
    		this.modelAttributeMethodCache
    				.computeIfAbsent(handlerType,
    						key -> MethodIntrospector.selectMethods(key, MODEL_ATTRIBUTE_METHODS))
    				.forEach(method -> {
    					Object bean = handlerMethod.getBean();
    					result.add(createAttributeMethod(bean, method));
    				});
    	}
}
