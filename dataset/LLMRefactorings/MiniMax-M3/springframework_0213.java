public class springframework_0213 {

    protected Mono<Void> doCommit(@Nullable Supplier<? extends Publisher<Void>> writeAction) {
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

        return Flux.concat(collectCommitActions()).then();
    }

    private List<Publisher<Void>> collectCommitActions() {
        List<Publisher<Void>> actions = new ArrayList<>(this.commitActions.size());
        for (Supplier<? extends Publisher<Void>> commitAction : this.commitActions) {
            actions.add(commitAction.get());
        }
        return actions;
    }
}
