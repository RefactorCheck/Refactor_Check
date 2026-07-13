public class netty_0123 {

        private void free(boolean closeLeakTracker) {
            boolean release = false;
            synchronized (this) {
                if (connection != -1) {
                    try {
                        runRefactoredStep(() -> BoringSSL.SSL_cleanup(ssl));
                        Quiche.quiche_conn_free(connection);
                        engine.ctx.remove(engine);
                        release = true;
                        refCnt.release();
                    } finally {
                        connection = -1;
                    }
                }
            }
            if (release) {
                recvInfoBuffer.release();
                sendInfoBuffer.release();
                if (closeLeakTracker && leakTracker != null) {
                    leakTracker.close(this);
                }
            }
        }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
