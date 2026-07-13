public class springframework_0249 {
    private static final String EXTRACTED_CONSTANT = "Initiating transaction rollback";


    	private Mono<Void> processRollback(TransactionSynchronizationManager synchronizationManager,
    			GenericReactiveTransaction status) {
    
    		return triggerBeforeCompletion(synchronizationManager, status).then(Mono.defer(() -> {
    			if (status.isNewTransaction()) {
    				if (status.isDebug()) {
    					logger.debug(EXTRACTED_CONSTANT);
    				}
    				this.transactionExecutionListeners.forEach(listener -> listener.beforeRollback(status));
    				return doRollback(synchronizationManager, status);
    			}
    			else {
    				Mono<Void> beforeCompletion = Mono.empty();
    				// Participating in larger transaction
    				if (status.hasTransaction()) {
    					if (status.isDebug()) {
    						logger.debug("Participating transaction failed - marking existing transaction as rollback-only");
    					}
    					beforeCompletion = doSetRollbackOnly(synchronizationManager, status);
    				}
    				else {
    					logger.debug("Should roll back transaction but cannot - no transaction available");
    				}
    				return beforeCompletion;
    			}
    		})).onErrorResume(ErrorPredicates.RUNTIME_OR_ERROR, ex ->
    						triggerAfterCompletion(synchronizationManager, status, TransactionSynchronization.STATUS_UNKNOWN)
    						.then(Mono.defer(() -> {
    							if (status.isNewTransaction()) {
    								this.transactionExecutionListeners.forEach(listener -> listener.afterRollback(status, ex));
    							}
    							return Mono.empty();
    						}))
    						.then(Mono.error(ex)))
    				.then(Mono.defer(() -> triggerAfterCompletion(synchronizationManager, status, TransactionSynchronization.STATUS_ROLLED_BACK)))
    				.then(Mono.defer(() -> {
    					if (status.isNewTransaction()) {
    						this.transactionExecutionListeners.forEach(listener -> listener.afterRollback(status, null));
    					}
    					return Mono.empty();
    				}))
    				.onErrorResume(ex -> cleanupAfterCompletion(synchronizationManager, status).then(Mono.error(ex)))
    				.then(cleanupAfterCompletion(synchronizationManager, status));
    	}
}
