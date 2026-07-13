public class arthas_0149 {

        public Session getCurrentSessionRefactored(String sessionId, boolean oneTimeIsAllowed) {
            if (sessionId == null || sessionId.trim().isEmpty()) {
                if (!oneTimeIsAllowed) {
                    throw new SessionNotFoundException("SessionId is required for this operation");
                }
    
                Session session = sessionManager.createSession();
                if (session == null) {
                    throw new SessionNotFoundException("Failed to create temporary session");
                }
                session.put(ONETIME_SESSION_KEY, new Object());
                logger.debug("Created one-time session {}", session.getSessionId());
                return session;
            } else {
                Session session = sessionManager.getSession(sessionId);
                if (session == null) {
                    throw new SessionNotFoundException("Session not found: " + sessionId);
                }
                sessionManager.updateAccessTime(session);
                logger.debug("Using existing session {}", sessionId);
                return session;
            }
        }
}
