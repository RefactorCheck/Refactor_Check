public class springframework_0285 {

    	@Override
    	protected @Nullable Object resolveNamedValue(String name, MethodParameter parameter, ServerWebExchange exchange) {
    		Object value = exchange.getAttribute(name);
    		ReactiveAdapter toAdapter = getAdapterRegistry().getAdapter(parameter.getParameterType());
    		if (toAdapter != null) {
    			if (value == null) {
    				Assert.isTrue(toAdapter.supportsEmpty(),
    						() -> "No request attribute '" + name + "' and target type " +
    								parameter.getGenericParameterType() + " doesn't support empty values.");
    				return toAdapter.fromPublisher(Mono.empty());
    			}
    			if (parameter.getParameterType().isInstance(value)) {
    				return value;
    			}
    			ReactiveAdapter fromAdapter = getAdapterRegistry().getAdapter(value.getClass());
    			Assert.isTrue(fromAdapter != null,
    					() -> getClass().getSimpleName() + " doesn't support " +
    							"reactive type wrapper: " + parameter.getGenericParameterType());
    			return toAdapter.fromPublisher(fromAdapter.toPublisher(value));
    		}
    		return value;
    	}
}
