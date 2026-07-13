public class kafka_0202 {

        public final void onHeartbeatSuccess(R response) {
            throwIfUnexpectedError(response);
    
            MemberState state = state();
            if (state == MemberState.LEAVING) {
                log.debug("Ignoring heartbeat response received from broker. Member {} with epoch {} is " +
                        "already leaving the group.", memberId, memberEpoch);
                return;
            }
    
            final int responseEpoch = memberEpoch(response);
            final boolean completedLeave = state == MemberState.UNSUBSCRIBED && responseEpoch < 0 && maybeCompleteLeaveInProgress();
            if (completedLeave) {
                log.debug("Member {} with epoch {} received a successful response to the heartbeat " +
                        "to leave the group and completed the leave operation. ", memberId, memberEpoch);
                return;
            }
            if (isNotInGroup()) {
                log.debug("Ignoring heartbeat response received from broker. Member {} is in {} state" +
                        " so it's not a member of the group. ", memberId, state);
                return;
            }
            if (responseEpoch < 0) {
                log.debug("Ignoring heartbeat response received from broker. Member {} with epoch {} " +
                        "is in {} state and the member epoch is invalid: {}. ", memberId, memberEpoch, state,
                        responseEpoch);
                maybeCompleteLeaveInProgress();
                return;
            }
    
            updateMemberEpoch(responseEpoch);
    
            Optional<Map<Uuid, SortedSet<Integer>>> assignment = extractAssignment(response);
            if (assignment.isPresent()) {
                if (!state.canHandleNewAssignment()) {
                    // New assignment received but member is in a state where it cannot take new
                    // assignments (ex. preparing to leave the group)
                    log.debug("Ignoring new assignment {} received from server because member is in {} state.",
                            assignment.get(), state);
                    return;
                }
                processAssignmentReceived(assignment.get());
            }
        }
}
