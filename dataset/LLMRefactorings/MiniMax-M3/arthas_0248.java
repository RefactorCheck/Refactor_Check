public class arthas_0248 {

        private ApiResponse processPullResultsRequest(ApiRequest apiRequest, Session session) throws ApiException {
            String consumerId = apiRequest.getConsumerId();
            if (StringUtils.isBlank(consumerId)) {
                throw new ApiException("'consumerId' is required");
            }
            ResultConsumer consumer = findConsumer(session, consumerId);

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

        private ResultConsumer findConsumer(Session session, String consumerId) throws ApiException {
            SharingResultDistributor resultDistributor = session.getResultDistributor();
            if (resultDistributor != null) {
                ResultConsumer consumer = resultDistributor.getConsumer(consumerId);
                if (consumer != null) {
                    return consumer;
                }
            }
            throw new ApiException("consumer not found: " + consumerId);
        }
}
