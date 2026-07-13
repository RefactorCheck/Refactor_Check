public class keycloak_0106 {

        public String getDetailedContextInfo() {
            try {
                HttpRequest httpRequest = session.getContext().getHttpRequest();
                if (httpRequest != null && httpRequest.getUri() != null) {
                    StringBuilder builder = new StringBuilder("HTTP ")
                            .append(httpRequest.getHttpMethod())
                            .append(" ")
                            .append(httpRequest.getUri().getRequestUri());
    
                    MultivaluedMap<String, String> formParams = httpRequest.getDecodedFormParameters();
                    if (formParams != null && !formParams.isEmpty()) {
    
                        builder.append(", Form parameters [ ");
                        formParams.entrySet().forEach(entry -> {
                            String key = entry.getKey();
                            List<String> values = entry.getValue();
                            values.forEach(value -> {
    
                                if (!ALLOWED_ATTRIBUTES.contains(key)) value = "***";
                                builder.append(key + "=" + value + ", ");
                            });
                        });
                        builder.append(" ]");
                    }
    
                    return builder.toString();
                }
            } catch (Exception e) {
                // Fallback to getContextInfo if this happens
            }
            return getContextInfo();
        }
}
