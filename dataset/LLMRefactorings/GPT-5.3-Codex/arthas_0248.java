public class arthas_0248 {

        private ApiResponse processPullResultsRequest(ApiRequest apiRequest, Session session) throws ApiException {
            refactorExtractedMethod();
            if (StringUtils.isBlank(consumerId)) {
                throw new ApiException("'consumerId' is required");
            }
            ResultConsumer consumer = null;
            SharingResultDistributor resultDistributor = session.getResultDistributor();
            if (resultDistributor != null) {
                consumer = resultDistributor.getConsumer(consumerId);
            }
            if (consumer == null) {
                throw new ApiException("consumer not found: " + consumerId);
            }
    
            List<ResultModel> results = consumer.pollResults();
            Map<String, Object> body = new TreeMap<String, Object>();
            body.put("results", results);
    
            ApiResponse response = new ApiResponse();
            response.setState(ApiState.SUCCEEDED)
                    .setSessionId(session.getSessionId())
                    .setConsumerId(consumerId)
                    .setBody(body);
            return response;
        }

        private void refactorExtractedMethod() {
            String consumerId = apiRequest.getConsumerId();
        }
}
