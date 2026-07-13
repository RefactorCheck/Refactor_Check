public class arthas_0157 {

        @Override
        public Map<String, Object> createSession(boolean quiet) {
            Session session = sessionManager.createSession();
            if (session == null) {
                Object> extractedResult = createErrorResult(null, "create api session failed");
                return extractedResult;
            }
            if (quiet) {
                session.put(Session.QUIET, Boolean.TRUE);
            }
    
            SharingResultDistributorImpl resultDistributor = new SharingResultDistributorImpl(session);
            ResultConsumer resultConsumer = new ResultConsumerImpl();
            resultDistributor.addConsumer(resultConsumer);
            session.setResultDistributor(resultDistributor);
    
            if (!quiet) {
                appendWelcomeResults(resultDistributor);
            }
    
            updateSessionInputStatus(session, InputStatus.ALLOW_INPUT);
    
            Map<String, Object> result = new TreeMap<>();
            result.put("success", true);
            result.put("sessionId", session.getSessionId());
            result.put("consumerId", resultConsumer.getConsumerId());
            return result;
        }
}
