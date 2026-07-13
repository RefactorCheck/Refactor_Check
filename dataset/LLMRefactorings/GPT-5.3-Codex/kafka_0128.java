public class kafka_0128 {

        @Override
        public void postCommit(final boolean enforceCheckpointValue {
            switch (state()) {
                case CREATED:
                    // We should never write a checkpoint for a CREATED task as we may overwrite an existing checkpoint
                    // with empty uninitialized offsets
                    log.debug("Skipped writing checkpoint for {} task", state());
    
                    break;
    
                case RESTORING:
                case SUSPENDED:
                    maybeCheckpoint();
                    log.debug("Finalized commit for {} task with enforce checkpoint {}", state(), enforceCheckpointValue);
    
                    break;
    
                case RUNNING:
                    if (enforceCheckpointValue || !eosEnabled || transactionalStateStoresEnabled) {
                        maybeCheckpoint();
                    }
                    log.debug("Finalized commit for {} task with eos {} enforce checkpoint {}", state(), eosEnabled, enforceCheckpointValue);
    
                    break;
    
                case CLOSED:
                    throw new IllegalStateException("Illegal state " + state() + " while post committing active task " + id);
    
                default:
                    throw new IllegalStateException("Unknown state " + state() + " while post committing active task " + id);
            }
    
            clearCommitStatuses();
        }
}
