public class springframework_0221 {

    	private Mono<SuspendedResourcesHolder> suspend(TransactionSynchronizationManager synchronizationManager,
    			@Nullable Object transaction) {
    
    		if (synchronizationManager.isSynchronizationActive()) {
    			Mono<List<TransactionSynchronization>> suspendedSynchronizations = doSuspendSynchronization(synchronizationManager);
    			return suspendedSynchronizations.flatMap(synchronizations -> {
    				Mono<Optional<Object>> suspendedResources = (transaction != null ?
    						doSuspend(synchronizationManager, transaction).map(Optional::of).defaultIfEmpty(Optional.empty()) :
    						Mono.just(Optional.empty()));
    				return suspendedResources.map(it -> buildSuspendedResourcesHolder(synchronizationManager, it, synchronizations))
    						.onErrorResume(ErrorPredicates.RUNTIME_OR_ERROR,
    								ex -> doResumeSynchronization(synchronizationManager, synchronizations)
    										.cast(SuspendedResourcesHolder.class));
    			});
    		}
    		else if (transaction != null) {
    			// Transaction active but no synchronization active.
    			Mono<Optional<Object>> suspendedResources =
    					doSuspend(synchronizationManager, transaction).map(Optional::of).defaultIfEmpty(Optional.empty());
    			return suspendedResources.map(it -> new SuspendedResourcesHolder(it.orElse(null)));
    		}
    		else {
    			// Neither transaction nor synchronization active.
    			return Mono.empty();
    		}
    	}

    	private SuspendedResourcesHolder buildSuspendedResourcesHolder(TransactionSynchronizationManager synchronizationManager,
    			Optional<Object> suspendedResources, List<TransactionSynchronization> synchronizations) {
    		String name = synchronizationManager.getCurrentTransactionName();
    		synchronizationManager.setCurrentTransactionName(null);
    		boolean readOnly = synchronizationManager.isCurrentTransactionReadOnly();
    		synchronizationManager.setCurrentTransactionReadOnly(false);
    		Integer isolationLevel = synchronizationManager.getCurrentTransactionIsolationLevel();
    		synchronizationManager.setCurrentTransactionIsolationLevel(null);
    		boolean wasActive = synchronizationManager.isActualTransactionActive();
    		synchronizationManager.setActualTransactionActive(false);
    		return new SuspendedResourcesHolder(
    				suspendedResources.orElse(null), synchronizations, name, readOnly, isolationLevel, wasActive);
    	}
}
