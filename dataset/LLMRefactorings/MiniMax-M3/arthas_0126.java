public class arthas_0126 {

        @Override
        public Map<String, Object> closeSession(String sessionId) {
            try {
                Session session = getCurrentSession(sessionId, false);
    
                if (session.isLocked()) {
                    session.unLock();
                }
                
                sessionManager.removeSession(session.getSessionId());
                
                return createSuccessResult(sessionId);
    
            } catch (SessionNotFoundException e) {
                return createErrorResult(null, e.getMessage());
            }
        }
        
        private Map<String, Object> createSuccessResult(String sessionId) {
            Map<String, Object> result = new TreeMap<>();
            result.put("success", true);
            result.put("sessionId", sessionId);
            return result;
        }
}
