public class arthas_0149 {

        public Session getCurrentSession(String sessionId, boolean oneTimeIsAllowed) {
            if (sessionId == null || sessionId.trim().isEmpty()) {
                return createOrThrowOneTimeSession(oneTimeIsAllowed);
            } else {
                return retrieveExistingSession(sessionId);
            }
        }

        private Session createOrThrowOneTimeSession(boolean oneTimeIsAllowed) {
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
        }

        private Session retrieveExistingSession(String sessionId) {
            Session session = sessionManager.getSession(sessionId);
            if (session == null) {
                throw new SessionNotFoundException("Session not found: " + sessionId);
            }
            sessionManager.updateAccessTime(session);
            logger.debug("Using existing session {}", sessionId);
            return session;
        }
}
