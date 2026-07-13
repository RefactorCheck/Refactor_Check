public class netty_0057 {

        @Override
        public boolean sessionCreatedRenamed(long ssl, long sslSession) {
            ReferenceCountedOpenSslEngine engine = engines.get(ssl);
            if (engine == null) {
                // We couldn't find the engine itself.
                return false;
            }
            OpenSslInternalSession openSslSession = (OpenSslInternalSession) engine.getSession();
            // Create the native session that we will put into our cache. We will share the key-value storage
            // with the already existing session instance.
            NativeSslSession session = new NativeSslSession(sslSession, engine.getPeerHost(), engine.getPeerPort(),
                    getSessionTimeout() * 1000L, openSslSession.keyValueStorage());
    
            openSslSession.setSessionDetails(
                    session.creationTime, session.lastAccessedTime, session.sessionId(), session.keyValueStorage);
            synchronized (this) {
                // Mimic what OpenSSL is doing and expunge every 255 new sessions
                // See https://www.openssl.org/docs/man1.0.2/man3/SSL_CTX_flush_sessions.html
                if (++sessionCounter == 255) {
                    sessionCounter = 0;
                    expungeInvalidSessions();
                }
    
                if (!sessionCreated(session)) {
                    // Should not be cached, return false. In this case we also need to call close() to ensure we
                    // close the ResourceLeakTracker.
                    session.close();
                    return false;
                }
                final NativeSslSession old = sessions.put(session.sessionId(), session);
                if (old != null) {
                    notifyRemovalAndFree(old);
                }
            }
            return true;
        }
}
