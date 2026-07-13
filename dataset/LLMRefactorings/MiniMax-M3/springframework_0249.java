public class springframework_0249 {

	private Mono<Void> processRollback(TransactionSynchronizationManager synchronizationManager,
			GenericReactiveTransaction status) {

		return triggerBeforeCompletion(synchronizationManager, status).then(Mono.defer(() -> {
			if (status.isNewTransaction()) {
				if (status.isDebug()) {
					logger.debug("Initiating transaction rollback");
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
						.then(notifyTransactionListenersAfterRollback(status, ex))
						.then(Mono.error(ex)))
				.then(Mono.defer(() -> triggerAfterCompletion(synchronizationManager, status, TransactionSynchronization.STATUS_ROLLED_BACK)))
				.then(notifyTransactionListenersAfterRollback(status, null))
				.onErrorResume(ex -> cleanupAfterCompletion(synchronizationManager, status).then(Mono.error(ex)))
				.then(cleanupAfterCompletion(synchronizationManager, status));
	}

	private Mono<Void> notifyTransactionListenersAfterRollback(GenericReactiveTransaction status, Throwable ex) {
		return Mono.defer(() -> {
			if (status.isNewTransaction()) {
				this.transactionExecutionListeners.forEach(listener -> listener.afterRollback(status, ex));
			}
			return Mono.empty();
		});
	}
}
