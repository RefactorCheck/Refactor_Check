public class arthas_0157 {

        @Override
        public Map<String, Object> createSession(boolean quiet) {
            Session session = sessionManager.createSession();
            if (session == null) {
                return createErrorResult(null, "create api session failed");
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
    
            return buildSessionResult(session, resultConsumer);
        }
    
        private Map<String, Object> buildSessionResult(Session session, ResultConsumer resultConsumer) {
            Map<String, Object> result = new TreeMap<>();
            result.put("success", true);
            result.put("sessionId", session.getSessionId());
            result.put("consumerId", resultConsumer.getConsumerId());
            return result;
        }
}
