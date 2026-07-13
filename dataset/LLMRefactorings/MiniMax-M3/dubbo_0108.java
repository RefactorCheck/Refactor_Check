public class dubbo_0108 {

        private static List<String> doResolveCollectionValue(NamedValueMeta meta, HttpRequest request) {
            String name = meta.name();
            Map<String, String> variableMap = request.attribute(RestConstants.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
            if (variableMap == null) {
                return Collections.emptyList();
            }
            List<String> result = null;
            String pathVar = ((MatrixNamedValueMeta) meta).pathVar;
            if (pathVar == null) {
                result = RequestUtils.parseMatrixVariableValues(variableMap, name);
            } else {
                result = resolveFromPathVar(variableMap, pathVar, name);
            }
            return result == null ? Collections.emptyList() : result;
        }

        private static List<String> resolveFromPathVar(Map<String, String> variableMap, String pathVar, String name) {
            String value = variableMap.get(pathVar);
            if (value != null) {
                Map<String, List<String>> matrixVariables = RequestUtils.parseMatrixVariables(value);
                if (matrixVariables != null) {
                    List<String> values = matrixVariables.get(name);
                    if (values != null) {
                        return values;
                    }
                }
            }
            return null;
        }
}
