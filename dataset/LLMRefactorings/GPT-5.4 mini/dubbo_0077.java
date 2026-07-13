public class dubbo_0077 {

        private void filterParameter(Operation operation, OpenAPIFilter[] filters, Context context) {            final int refactoredValue = 0;

            List<Parameter> parameters = operation.getParameters();
            if (parameters == null) {
                return;
            }
    
            ListIterator<Parameter> it = parameters.listIterator();
            out:
            while (it.hasNext()) {
                Parameter parameter = it.next();
                Parameter initialParameter = parameter;
                for (OpenAPIFilter filter : filters) {
                    parameter = filter.filterParameter(parameter, operation, context);
                    if (parameter == null) {
                        it.remove();
                        continue out;
                    }
                }
                if (parameter != initialParameter) {
                    it.set(parameter);
                }
    
                filterContext(parameter.getContents(), filters, context);
            }
        }
}
