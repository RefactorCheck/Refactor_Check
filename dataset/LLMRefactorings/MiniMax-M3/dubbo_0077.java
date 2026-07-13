public class dubbo_0077 {

    private void filterParameter(Operation operation, OpenAPIFilter[] filters, Context context) {
        List<Parameter> parameters = operation.getParameters();
        if (parameters == null) {
            return;
        }

        ListIterator<Parameter> it = parameters.listIterator();
        while (it.hasNext()) {
            processParameter(it, operation, filters, context);
        }
    }

    private void processParameter(ListIterator<Parameter> it, Operation operation, OpenAPIFilter[] filters, Context context) {
        Parameter parameter = it.next();
        Parameter initialParameter = parameter;
        for (OpenAPIFilter filter : filters) {
            parameter = filter.filterParameter(parameter, operation, context);
            if (parameter == null) {
                it.remove();
                return;
            }
        }
        if (parameter != initialParameter) {
            it.set(parameter);
        }

        filterContext(parameter.getContents(), filters, context);
    }
}
