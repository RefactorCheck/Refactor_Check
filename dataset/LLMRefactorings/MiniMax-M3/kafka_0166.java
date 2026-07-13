public class kafka_0166 {

        @Override
        public CommitPartitionValidator validateOffsetCommit(
            String memberId,
            String groupInstanceId,
            int generationId,
            boolean isTransactional,
            int apiVersion
        ) throws CoordinatorNotAvailableException, UnknownMemberIdException, IllegalGenerationException, FencedInstanceIdException {
            if (isInState(DEAD)) {
                throw Errors.COORDINATOR_NOT_AVAILABLE.exception();
            }
    
            if (generationId < 0 && isInState(EMPTY)) {
                return CommitPartitionValidator.NO_OP;
            }
    
            if (generationId >= 0 || !memberId.isEmpty() || groupInstanceId != null) {
                validateMember(memberId, groupInstanceId, isTransactional ? "offset-commit" : "txn-offset-commit");
    
                if (generationId != this.generationId) {
                    throw Errors.ILLEGAL_GENERATION.exception();
                }
            } else if (!isTransactional && !isInState(EMPTY)) {
                throw Errors.UNKNOWN_MEMBER_ID.exception();
            }
    
            validateNotCompletingRebalance(isTransactional);
    
            return CommitPartitionValidator.NO_OP;
        }
    
        private void validateNotCompletingRebalance(boolean isTransactional) {
            if (!isTransactional && isInState(COMPLETING_REBALANCE)) {
                throw Errors.REBALANCE_IN_PROGRESS.exception();
            }
        }
}
