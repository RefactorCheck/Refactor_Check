public class netty_0057 {

        @Override
        public boolean sessionCreated(long ssl, long sslSession) {
            ReferenceCountedOpenSslEngine engine = engines.get(ssl);
            if (engine == null) {
                return false;
            }
            OpenSslInternalSession openSslSession = (OpenSslInternalSession) engine.getSession();
            NativeSslSession session = new NativeSslSession(sslSession, engine.getPeerHost(), engine.getPeerPort(),
                    getSessionTimeout() * 1000L, openSslSession.keyValueStorage());

            openSslSession.setSessionDetails(
                    session.creationTime, session.lastAccessedTime, session.sessionId(), session.keyValueStorage);
            return cacheSession(session);
        }

        private synchronized boolean cacheSession(NativeSslSession session) {
            if (++sessionCounter == 255) {
                sessionCounter = 0;
                expungeInvalidSessions();
            }

            if (!sessionCreated(session)) {
                session.close();
                return false;
            }
            final NativeSslSession old = sessions.put(session.sessionId(), session);
            if (old != null) {
                notifyRemovalAndFree(old);
            }
            return true;
        }
}
