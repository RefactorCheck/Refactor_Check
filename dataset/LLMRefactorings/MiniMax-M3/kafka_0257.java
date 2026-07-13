public class kafka_0257 {

        void abortTransaction() {
            if (!eosEnabled()) {
                throw new IllegalStateException(formatException("Exactly-once is not enabled"));
            }
            if (transactionInFlight) {
                handleTransactionAbort();
                transactionInFlight = false;
            }
        }

        private void handleTransactionAbort() {
            try {
                producer.abortTransaction();
            } catch (final TimeoutException logAndSwallow) {
                // no need to re-throw because we abort a TX only if we close a task dirty,
                // and thus `task.timeout.ms` does not apply
                log.warn(
                    "Aborting transaction failed due to timeout." +
                        " Will rely on broker to eventually abort the transaction after the transaction timeout passed.",
                    logAndSwallow
                );
            } catch (final ProducerFencedException | InvalidProducerEpochException | InvalidPidMappingException error) {
                // The producer is aborting the txn when there's still an ongoing one,
                // which means that we did not commit the task while closing it, which
                // means that it is a dirty close. Therefore it is possible that the dirty
                // close is due to an fenced exception already thrown previously, and hence
                // when calling abortTxn here the same exception would be thrown again.
                // Even if the dirty close was not due to an observed fencing exception but
                // something else (e.g. task corrupted) we can still ignore the exception here
                // since transaction already got aborted by brokers/transactional-coordinator if this happens
                log.debug("Encountered {} while aborting the transaction; this is expected and hence swallowed", error.getMessage());
            } catch (final KafkaException error) {
                throw new StreamsException(
                    formatException("Error encounter trying to abort a transaction"),
                    error
                );
            }
        }
}
