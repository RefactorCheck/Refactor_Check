public class kafka_0253 {

        private void revokeUnassignablePartitions() {
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
                            memberAssignedPartitions++;
                            addPartitionToAssignments(memberIndex, topicId, partition);
                            if (memberAssignedPartitions >= desiredAssignmentCountForMember) {
                                if (newMemberAssignment == null) {
                                    newMemberAssignment = AssignorHelpers.deepCopyAssignment(oldMemberAssignment);
                                }
                            }
                        }
                    } else {
                        if (newMemberAssignment == null) {
                            newMemberAssignment = AssignorHelpers.deepCopyAssignment(oldMemberAssignment);
                        }
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

        private void addPartitionToAssignments(final int memberIndex, final Uuid topicId, final int partition) {
            TopicIdPartition topicPartition = new TopicIdPartition(topicId, partition);
            finalAssignmentByPartition.computeIfAbsent(topicPartition, k -> new HashSet<>()).add(memberIndex);
            finalAssignmentByMember.computeIfAbsent(memberIndex, k -> new HashSet<>()).add(topicPartition);
        }
}
