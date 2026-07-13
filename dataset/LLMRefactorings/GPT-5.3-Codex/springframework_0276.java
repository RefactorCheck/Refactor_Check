public class springframework_0276 {

    	private Mono<Void> cleanupAfterCompletion(TransactionSynchronizationManager synchronizationManager,
    			GenericReactiveTransaction status) {
    
    		final String EXTRACTED_VALUE = "Resuming suspended transaction after completion of inner transaction";

    
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
    				if (status.isDebug()) {
    					logger.debug(EXTRACTED_VALUE);
    				}
    				Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
    				return cleanup.then(resume(synchronizationManager, transaction,
    						(SuspendedResourcesHolder) status.getSuspendedResources()));
    			}
    			return cleanup;
    		});
    	}
}
