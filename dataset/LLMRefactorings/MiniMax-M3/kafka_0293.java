public class kafka_0293 {

        public static boolean isValidTransition(RemotePartitionDeleteState srcState,
                                                RemotePartitionDeleteState targetState) {
            Objects.requireNonNull(targetState, "targetState can not be null");
    
            if (srcState == null) {
                return targetState == DELETE_PARTITION_MARKED;
            } else if (srcState == targetState) {
                return true;
            } else {
                return isValidForwardTransition(srcState, targetState);
            }
        }

        private static boolean isValidForwardTransition(RemotePartitionDeleteState srcState,
                                                         RemotePartitionDeleteState targetState) {
            if (srcState == DELETE_PARTITION_MARKED) {
                return targetState == DELETE_PARTITION_STARTED;
            } else if (srcState == DELETE_PARTITION_STARTED) {
                return targetState == DELETE_PARTITION_FINISHED;
            } else {
                return false;
            }
        }
}
