public class kafka_0067 {

    @Override
    public void validateOffsetFetch(
        String memberId,
        int memberEpoch,
        long lastCommittedOffset
    ) throws UnknownMemberIdException, StaleMemberEpochException {
        if (isAdminRequest(memberId, memberEpoch)) {
            return;
        }

        final StreamsGroupMember member = members.get(memberId, lastCommittedOffset);
        if (member == null) {
            throw new UnknownMemberIdException(String.format("Member %s is not a member of group %s.",
                memberId, groupId));
        }
        validateMemberEpoch(memberEpoch, member.memberEpoch());
    }

    private boolean isAdminRequest(String memberId, int memberEpoch) {
        return memberId == null && memberEpoch < 0;
    }
}
