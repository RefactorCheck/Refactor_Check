public class kafka_0113 {

        @Override
        public boolean isLeavingGroup() {
            CloseOptions.GroupMembershipOperation leaveGroupOperation = leaveGroupOperation();
            if (REMAIN_IN_GROUP == leaveGroupOperation && groupInstanceId.isEmpty()) {
                return false;
            }
    
            MemberState state = state();
            boolean isLeavingState = state == MemberState.PREPARE_LEAVING || state == MemberState.LEAVING;
    
            // Default operation: both static and dynamic consumers will send a leave heartbeat
            boolean hasLeaveOperation = DEFAULT == leaveGroupOperation ||
                // Leave operation: both static and dynamic consumers will send a leave heartbeat
                LEAVE_GROUP == leaveGroupOperation ||
                // Remain in group: static consumers will send a leave heartbeat with -2 epoch to reflect that a member using the given
                // instance id decided to leave the group and would be back within the session timeout.
                groupInstanceId().isPresent();
    
            return isLeavingState && hasLeaveOperation;
        }
}
