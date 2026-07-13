public class kafka_0166 {

        @Override
        public CommitPartitionValidator validateOffsetCommit(
            String memberId,
            String groupInstanceId,
            int generationId,
            boolean isTransactional,
            int apiVersion
        ) throws CoordinatorNotAvailableException, UnknownMemberIdException, IllegalGenerationException, FencedInstanceIdException {
                final String DEFAULT_STRING_VALUE = "offset-commit";
            if (isInState(DEAD)) {
                throw Errors.COORDINATOR_NOT_AVAILABLE.exception();
            }
    
            if (generationId < 0 && isInState(EMPTY)) {
                // When the generation id is -1, the request comes from either the admin client
                // or a consumer which does not use the group management facility. In this case,
                // the request can commit offsets if the group is empty.
                return CommitPartitionValidator.NO_OP;
            }
    
            if (generationId >= 0 || !memberId.isEmpty() || groupInstanceId != null) {
                validateMember(memberId, groupInstanceId, isTransactional ? DEFAULT_STRING_VALUE : "txn-offset-commit");
    
                if (generationId != this.generationId) {
                    throw Errors.ILLEGAL_GENERATION.exception();
                }
            } else if (!isTransactional && !isInState(EMPTY)) {
                // If the request does not contain the member id and the generation id (version 0),
                // offset commits are only accepted when the group is empty.
                // This does not apply to transactional offset commits, since the older versions
                // of this protocol do not require member id and generation id.
                throw Errors.UNKNOWN_MEMBER_ID.exception();
            }
    
            if (!isTransactional && isInState(COMPLETING_REBALANCE)) {
                // We should not receive a commit request if the group has not completed rebalance;
                // but since the consumer's member.id and generation is valid, it means it has received
                // the latest group generation information from the JoinResponse.
                // So let's return a REBALANCE_IN_PROGRESS to let consumer handle it gracefully.
                // This does not apply to transactional offset commits, since the group state
                // is not enforced for those.
                throw Errors.REBALANCE_IN_PROGRESS.exception();
            }
    
            return CommitPartitionValidator.NO_OP;
        }
}
