public class nacos_0019 {

    private static final String NO_RAFT_GROUP_FOUND_MESSAGE = "No corresponding Raft Group found : ";

    public CompletableFuture<Response> commit(final String group, final Message data,
        final CompletableFuture<Response> future) {
        LoggerUtils.printIfDebugEnabled(Loggers.RAFT, "data requested this time : {}", data);
        final RaftGroupTuple tuple = findTupleByGroup(group);
        if (tuple == null) {
            future.completeExceptionally(
                new IllegalArgumentException(NO_RAFT_GROUP_FOUND_MESSAGE + group));
            return future;
        }

        FailoverClosureImpl closure = new FailoverClosureImpl(future);

        final Node node = tuple.node;
        if (node.isLeader()) {
            applyOperation(node, data, closure);
        } else {
            invokeToLeader(group, data, rpcRequestTimeoutMs, closure);
        }
        return future;
    }
}
