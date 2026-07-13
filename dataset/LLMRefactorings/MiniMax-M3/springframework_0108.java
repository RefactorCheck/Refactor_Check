public class springframework_0108 {

    @Override
    protected @Nullable Object resolveNamedValue(String name, MethodParameter param, ServerWebExchange exchange) {
        Map<String, MultiValueMap<String, String>> pathParameters =
                exchange.getAttribute(HandlerMapping.MATRIX_VARIABLES_ATTRIBUTE);
        if (CollectionUtils.isEmpty(pathParameters)) {
            return null;
        }

        MatrixVariable ann = param.getParameterAnnotation(MatrixVariable.class);
        Assert.state(ann != null, "No MatrixVariable annotation");
        String pathVar = ann.pathVar();
        List<String> paramValues = findMatrixVariableValues(pathParameters, pathVar, name, param);

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

    private List<String> findMatrixVariableValues(
            Map<String, MultiValueMap<String, String>> pathParameters,
            String pathVar, String name, MethodParameter param) {
        List<String> paramValues = null;

        if (!pathVar.equals(ValueConstants.DEFAULT_NONE)) {
            if (pathParameters.containsKey(pathVar)) {
                paramValues = pathParameters.get(pathVar).get(name);
            }
        }
        else {
            boolean found = false;
            paramValues = new ArrayList<>();
            for (MultiValueMap<String, String> params : pathParameters.values()) {
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
        return paramValues;
    }
}
