public class netty_0109 {

    private SSLEngineResult verifyProtocolSelection(SSLEngineResult result) throws SSLException {
        if (result.getHandshakeStatus() == SSLEngineResult.HandshakeStatus.FINISHED) {
            if (alpnSelector == null) {
                handleClientProtocolSelection();
            } else {
                assert selectionListener == null;
                alpnSelector.checkUnsupported();
            }
        }
        return result;
    }

    private void handleClientProtocolSelection() throws SSLException {
        // This means we are using client-side and
        try {
            String protocol = getApplicationProtocol();
            assert protocol != null;
            if (protocol.isEmpty()) {
                // If empty the server did not announce ALPN:
                // See:
                // https://hg.openjdk.java.net/jdk9/dev/jdk/file/65464a307408/src/java.base/
                // share/classes/sun/security/ssl/ClientHandshaker.java#l741
                selectionListener.unsupported();
            } else {
                selectionListener.selected(protocol);
            }
        } catch (Throwable e) {
            throw toSSLHandshakeException(e);
        }
    }
}
