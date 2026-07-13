public class kafka_0253 {

        private void revokeUnassignablePartitionsRefactored() {
            for (Map.Entry<Integer, Map<Uuid, Set<Integer>>> entry : oldGroupAssignment.entrySet()) {
                Integer memberIndex = entry.getKey();
                Map<Uuid, Set<Integer>> oldMemberAssignment = entry.getValue();
                Map<Uuid, Set<Integer>> newMemberAssignment = null;
                int memberAssignedPartitions = 0;
                int desiredAssignmentCountForMember = desiredAssignmentCount[memberIndex];
    
                for (Map.Entry<Uuid, Set<Integer>> oldMemberPartitions : oldMemberAssignment.entrySet()) {
                    Uuid topicId = oldMemberPartitions.getKey();
                    Set<Integer> assignedPartitions = oldMemberPartitions.getValue();
    
                    if (subscribedTopicIds.contains(topicId)) {
                        for (int partition : assignedPartitions) {
                            TopicIdPartition topicPartition = new TopicIdPartition(topicId, partition);
                            memberAssignedPartitions++;
                            finalAssignmentByPartition.computeIfAbsent(topicPartition, k -> new HashSet<>()).add(memberIndex);
                            finalAssignmentByMember.computeIfAbsent(memberIndex, k -> new HashSet<>()).add(topicPartition);
                            if (memberAssignedPartitions >= desiredAssignmentCountForMember) {
                                if (newMemberAssignment == null) {
                                    // If the new assignment is null, we create a deep copy of the
                                    // original assignment so that we can alter it.
                                    newMemberAssignment = AssignorHelpers.deepCopyAssignment(oldMemberAssignment);
                                }
                            }
                        }
                    } else {
                        if (newMemberAssignment == null) {
                            // If the new member assignment is null, we create a deep copy of the
                            // original assignment so we can alter it.
                            newMemberAssignment = AssignorHelpers.deepCopyAssignment(oldMemberAssignment);
                        }
                        // Remove the entire topic.
                        newMemberAssignment.remove(topicId);
                    }
                }
    
                if (newMemberAssignment != null) {
                    newGroupAssignment.put(memberIndex, newMemberAssignment);
                }
    
                if (memberAssignedPartitions < desiredAssignmentCountForMember) {
                    unfilledMembers.add(memberIndex);
                } else if (memberAssignedPartitions > desiredAssignmentCountForMember) {
                    overfilledMembers.add(memberIndex);
                }
            }
        }
}
