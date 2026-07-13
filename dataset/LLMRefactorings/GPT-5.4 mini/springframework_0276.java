public class springframework_0276 {
    private static final String EXTRACTED_CONSTANT = "Resuming suspended transaction after completion of inner transaction";


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
    				if (status.isDebug()) {
    					logger.debug(EXTRACTED_CONSTANT);
    				}
    				Object transaction = (status.hasTransaction() ? status.getTransaction() : null);
    				return cleanup.then(resume(synchronizationManager, transaction,
    						(SuspendedResourcesHolder) status.getSuspendedResources()));
    			}
    			return cleanup;
    		});
    	}
}
