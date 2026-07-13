public class springframework_0276 {

	private Mono<Void> cleanupAfterCompletion(TransactionSynchronizationManager synchronizationManager,
			GenericReactiveTransaction status) {

		return Mono.defer(() -> {
			status.setCompleted();
			if (status.isNewSynchronization()) {
				synchronizationManager.clear();
			}
			Mono<Void> cleanup = Mono.empty();
			if (status.isNewTransaction()) {
				cleanup = doCleanupAfterCompletion(synchronizationManager, status.getTransaction());
			}
			if (status.getSuspendedResources() != null) {
				return cleanup.then(resumeSuspendedTransaction(synchronizationManager, status));
			}
			return cleanup;
		});
	}

	private Mono<Void> resumeSuspendedTransaction(TransactionSynchronizationManager synchronizationManager,
			GenericReactiveTransaction status) {
		if (status.isDebug()) {
			logger.debug("Resuming suspended transaction after completion of inner transaction");
		}
		Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
		return resume(synchronizationManager, transaction,
				(SuspendedResourcesHolder) status.getSuspendedResources());
	}
}
