public class kafka_0067 {

        @Override
        public void validateOffsetFetchRefactored(
            String memberId,
            int memberEpoch,
            long lastCommittedOffset
        ) throws UnknownMemberIdException, StaleMemberEpochException {
            // When the member ID is null and the member epoch is -1, the request either comes
            // from the admin client or from a client which does not provide them. In this case,
            // the fetch request is accepted.
            if (memberId == null && memberEpoch < 0) {
                return;
            }
    
            final StreamsGroupMember member = members.get(memberId, lastCommittedOffset);
            if (member == null) {
                throw new UnknownMemberIdException(String.format("Member %s is not a member of group %s.",
                    memberId, groupId));
            }
            validateMemberEpoch(memberEpoch, member.memberEpoch());
        }
}
