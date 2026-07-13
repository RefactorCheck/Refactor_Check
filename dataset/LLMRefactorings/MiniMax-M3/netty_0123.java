public class netty_0123 {

    private void free(boolean closeLeakTracker) {
        boolean release = cleanupConnection();
        if (release) {
            releaseBuffers(closeLeakTracker);
        }
    }

    private synchronized boolean cleanupConnection() {
        boolean release = false;
        if (connection != -1) {
            try {
                BoringSSL.SSL_cleanup(ssl);
                Quiche.quiche_conn_free(connection);
                engine.ctx.remove(engine);
                release = true;
                refCnt.release();
            } finally {
                connection = -1;
            }
        }
        return release;
    }

    private void releaseBuffers(boolean closeLeakTracker) {
        recvInfoBuffer.release();
        sendInfoBuffer.release();
        if (closeLeakTracker && leakTracker != null) {
            leakTracker.close(this);
        }
    }
}
