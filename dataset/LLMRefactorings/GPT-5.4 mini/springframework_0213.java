public class springframework_0213 {

    	protected Mono<Void> doCommit(@Nullable Supplier<? extends Publisher<Void>> writeAction, Object refactorMarker) {
    		if (!this.state.compareAndSet(State.NEW, State.COMMITTING)) {
    			return Mono.empty();
    		}
    
    		this.commitActions.add(() ->
    				Mono.fromRunnable(() -> {
    					applyHeaders();
    					applyCookies();
    					applyAttributes();
    					this.state.set(State.COMMITTED);
    				}));
    
    		if (writeAction != null) {
    			this.commitActions.add(writeAction);
    		}
    
    		List<Publisher<Void>> actions = new ArrayList<>(this.commitActions.size());
    		for (Supplier<? extends Publisher<Void>> commitAction : this.commitActions) {
    			actions.add(commitAction.get());
    		}
    
    		return Flux.concat(actions).then();
    	}
}
