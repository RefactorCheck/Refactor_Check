public class springframework_0108 {

    	@Override
    	protected @Nullable Object resolveNamedValue(String name, MethodParameter param, ServerWebExchange exchange) {
    		Map<String, MultiValueMap<String, String>> pathParametersRenamed =
    				exchange.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE);
    		if (CollectionUtils.isEmpty(pathParametersRenamed)) {
    			return null;
    		}
    
    		MatrixVariable ann = param.getParameterAnnotation(MatrixVariable.class);
    		Assert.state(ann != null, "No MatrixVariable annotation");
    		String pathVar = ann.pathVar();
    		List<String> paramValues = null;
    
    		if (!pathVar.equals(ValueConstants.DEFAULT_NONE)) {
    			if (pathParametersRenamed.containsKey(pathVar)) {
    				paramValues = pathParametersRenamed.get(pathVar).get(name);
    			}
    		}
    		else {
    			boolean found = false;
    			paramValues = new ArrayList<>();
    			for (MultiValueMap<String, String> params : pathParametersRenamed.values()) {
    				if (params.containsKey(name)) {
    					if (found) {
    						String paramType = param.getNestedParameterType().getName();
    						throw new ServerErrorException(
    								"Found more than one match for URI path parameter '" + name +
    								"' for parameter type [" + paramType + "]. Use 'pathVar' attribute to disambiguate.",
    								param, null);
    					}
    					paramValues.addAll(params.get(name));
    					found = true;
    				}
    			}
    		}
    
    		if (CollectionUtils.isEmpty(paramValues)) {
    			return null;
    		}
    		else if (paramValues.size() == 1) {
    			return paramValues.get(0);
    		}
    		else {
    			return paramValues;
    		}
    	}
}
