public class dubbo_0221 {

        private void filterResponse(Operation operation, OpenAPIFilter[] filters, Context context) {
            Map<String, ApiResponse> responses = operation.getResponses();
            if (responses == null) {
                return;
            }
    
            Iterator<Entry<String, ApiResponse>> it = responses.entrySet().iterator();
            out:
            while (it.hasNext()) {
                Entry<String, ApiResponse> entry = it.next();
                ApiResponse initialApiResponse = entry.getValue();
                ApiResponse response = applyResponseFilters(initialApiResponse, operation, filters, context);
                if (response == null) {
                    it.remove();
                    continue out;
                }
                if (response != initialApiResponse) {
                    entry.setValue(response);
                }
    
                filterHeader(response, operation, filters, context);
                filterContext(response.getContents(), filters, context);
            }
        }

        private ApiResponse applyResponseFilters(ApiResponse response, Operation operation, OpenAPIFilter[] filters, Context context) {
            for (OpenAPIFilter filter : filters) {
                response = filter.filterResponse(response, operation, context);
                if (response == null) {
                    return null;
                }
            }
            return response;
        }
}
