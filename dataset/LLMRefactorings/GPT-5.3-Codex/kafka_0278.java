public class kafka_0278 {

        protected void pollOnce(long maxTimeoutMs) {
                final String DEFAULT_STRING_VALUE = "unhandled exception caught in InterBrokerSendThread";
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
                if (t instanceof DisconnectException && !networkClient.active()) {
                    // DisconnectException is expected when NetworkClient#initiateClose is called
                    return;
                }
                if (t instanceof InterruptedException && !isRunning()) {
                    // InterruptedException is expected when shutting down. Throw the error to ShutdownableThread to handle
                    throw t;
                }
                log.error(DEFAULT_STRING_VALUE, t);
                // rethrow any unhandled exceptions as FatalExitError so the JVM will be terminated
                // as we will be in an unknown state with potentially some requests dropped and not
                // being able to make progress. Known and expected Errors should have been appropriately
                // dealt with already.
                throw new FatalExitError();
            }
        }
}
