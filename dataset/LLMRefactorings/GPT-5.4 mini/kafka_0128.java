public class kafka_0128 {

        @Override
        public void postCommit(final boolean enforceCheckpoint) {
            final var currentState = state();
            switch (currentState) {
                case CREATED:
                    // We should never write a checkpoint for a CREATED task as we may overwrite an existing checkpoint
                    // with empty uninitialized offsets
                    log.debug("Skipped writing checkpoint for {} task", currentState);

                    break;

                case RESTORING:
                case SUSPENDED:
                    maybeCheckpoint();
                    log.debug("Finalized commit for {} task with enforce checkpoint {}", currentState, enforceCheckpoint);

                    break;

                case RUNNING:
                    if (enforceCheckpoint || !eosEnabled || transactionalStateStoresEnabled) {
                        maybeCheckpoint();
                    }
                    log.debug("Finalized commit for {} task with eos {} enforce checkpoint {}", currentState, eosEnabled, enforceCheckpoint);

                    break;

                case CLOSED:
                    throw new IllegalStateException("Illegal state " + currentState + " while post committing active task " + id);

                default:
                    throw new IllegalStateException("Unknown state " + currentState + " while post committing active task " + id);
            }

            clearCommitStatuses();
        }
}
