public class kafka_0066 {

            @Override
            public void run() {
                log.debug("Thread starting");
                try {
                    processRequests();
                } finally {
                    closeAdminClientThread();
                }
            }

            private void closeAdminClientThread() {
                closing = true;
                AppInfoParser.unregisterAppInfo(JMX_PREFIX, clientId, metrics);

                int numTimedOut = 0;
                TimeoutProcessor timeoutProcessor = new TimeoutProcessor(Long.MAX_VALUE);
                synchronized (this) {
                    numTimedOut += timeoutProcessor.handleTimeouts(newCalls, "The AdminClient thread has exited.");
                }
                numTimedOut += timeoutProcessor.handleTimeouts(pendingCalls, "The AdminClient thread has exited.");
                numTimedOut += timeoutCallsToSend(timeoutProcessor);
                numTimedOut += timeoutProcessor.handleTimeouts(correlationIdToCalls.values(),
                    "The AdminClient thread has exited.");
                if (numTimedOut > 0) {
                    log.info("Timed out {} remaining operation(s) during close.", numTimedOut);
                }
                closeQuietly(client, "KafkaClient");
                closeQuietly(metrics, "Metrics");
                log.debug("Exiting AdminClientRunnable thread.");
            }
}
