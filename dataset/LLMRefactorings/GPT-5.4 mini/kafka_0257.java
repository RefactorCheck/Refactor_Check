public class kafka_0257 {

        void abortTransaction() {
            if (!eosEnabled()) {
                throw new IllegalStateException(formatException("Exactly-once is not enabled"));
            }
            if (transactionInFlight) {
                try {
                    producer.abortTransaction();
                } catch (final TimeoutException logAndSwallow) {
                    handleAbortTransactionTimeout(logAndSwallow);
                } catch (final ProducerFencedException | InvalidProducerEpochException | InvalidPidMappingException error) {
                    handleAbortTransactionError(error);
                } catch (final KafkaException error) {
                    throw new StreamsException(
                        formatException("Error encounter trying to abort a transaction"),
                        error
                    );
                }
                transactionInFlight = false;
            }
        }

        private void handleAbortTransactionTimeout(final TimeoutException logAndSwallow) {
            log.warn(
                "Aborting transaction failed due to timeout." +
                    " Will rely on broker to eventually abort the transaction after the transaction timeout passed.",
                logAndSwallow
            );
        }

        private void handleAbortTransactionError(final RuntimeException error) {
            log.debug("Encountered {} while aborting the transaction; this is expected and hence swallowed", error.getMessage());
        }
}
