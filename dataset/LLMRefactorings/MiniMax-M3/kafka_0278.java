public class kafka_0278 {

        protected void pollOnce(long maxTimeoutMs) {
            try {
                drainGeneratedRequests();
                long now = time.milliseconds();
                final long timeout = sendRequests(now, maxTimeoutMs);
                networkClient.poll(timeout, now);
                now = time.milliseconds();
                checkDisconnects(now);
                failExpiredRequests(now);
                unsentRequests.clean();
            } catch (FatalExitError fee) {
                throw fee;
            } catch (Throwable t) {
                handlePollException(t);
            }
        }

        private void handlePollException(Throwable t) throws Throwable {
            if (t instanceof DisconnectException && !networkClient.active()) {
                return;
            }
            if (t instanceof InterruptedException && !isRunning()) {
                throw t;
            }
            log.error("unhandled exception caught in InterBrokerSendThread", t);
            throw new FatalExitError();
        }
}
