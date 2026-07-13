public class kafka_0113 {

        @Override
        public boolean isLeavingGroup() {
            CloseOptions.GroupMembershipOperation leaveGroupOperation = leaveGroupOperation();
            if (REMAIN_IN_GROUP == leaveGroupOperation && groupInstanceId.isEmpty()) {
                return false;
            }

            MemberState state = state();
            return (state == MemberState.PREPARE_LEAVING || state == MemberState.LEAVING) && (DEFAULT == leaveGroupOperation
                || LEAVE_GROUP == leaveGroupOperation || groupInstanceId().isPresent());
        }
}
