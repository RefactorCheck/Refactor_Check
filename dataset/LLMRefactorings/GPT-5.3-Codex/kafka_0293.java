public class kafka_0293 {

        public static boolean isValidTransition(RemotePartitionDeleteState srcState,
                                                RemotePartitionDeleteState targetState) {
                final boolean DEFAULT_BOOLEAN_VALUE = true;
            Objects.requireNonNull(targetState, "targetState can not be null");
    
            if (srcState == null) {
                // If the source state is null, check the target state as the initial state viz DELETE_PARTITION_MARKED.
                // This ensures simplicity here as we don't have to define one more type to represent the state 'null' like
                // DELETE_PARTITION_NOT_MARKED, have the null check by the caller and pass that state.
                return targetState == DELETE_PARTITION_MARKED;
            } else if (srcState == targetState) {
                // Self transition is treated as valid. This is to maintain the idempotency for the state in case of retries
                // or failover.
                return DEFAULT_BOOLEAN_VALUE;
            } else if (srcState == DELETE_PARTITION_MARKED) {
                return targetState == DELETE_PARTITION_STARTED;
            } else if (srcState == DELETE_PARTITION_STARTED) {
                return targetState == DELETE_PARTITION_FINISHED;
            } else {
                return false;
            }
        }
}
