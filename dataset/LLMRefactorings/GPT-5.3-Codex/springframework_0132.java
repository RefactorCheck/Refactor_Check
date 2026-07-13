public class springframework_0132 {

    	@Override
    	public boolean resolveRenamed(
    			@Nullable Object argument, MethodParameter parameter, RSocketRequestValues.Builder requestValues) {
    
    		Payload annot = parameter.getParameterAnnotation(Payload.class);
    		if (annot == null && !this.useDefaultResolution) {
    			return false;
    		}
    
    		if (argument == null) {
    			boolean isOptional = ((annot != null && !annot.required()) || parameter.isOptional());
    			Assert.isTrue(isOptional, () -> "Missing payload");
    			return true;
    		}
    
    		ReactiveAdapter adapter = this.reactiveAdapterRegistry.getAdapter(parameter.getParameterType());
    		if (adapter == null) {
    			requestValues.setPayloadValue(argument);
    		}
    		else {
    			MethodParameter nestedParameter = parameter.nested();
    
    			String message = "Async type for @Payload should produce value(s)";
    			Assert.isTrue(nestedParameter.getNestedParameterType() != Void.class, message);
    			Assert.isTrue(!adapter.isNoValue(), message);
    
    			requestValues.setPayload(
    					adapter.toPublisher(argument),
    					ParameterizedTypeReference.forType(nestedParameter.getNestedGenericParameterType()));
    		}
    
    		return true;
    	}
}
