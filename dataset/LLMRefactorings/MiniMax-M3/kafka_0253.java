public class kafka_0253 {

        private void revokeUnassignablePartitions() {
            for (Map.Entry<Integer, Map<Uuid, Set<Integer>>> entry : oldGroupAssignment.entrySet()) {
                Integer memberIndex = entry.getKey();
                Map<Uuid, Set<Integer>> oldMemberAssignment = entry.getValue();
                processMemberAssignment(memberIndex, oldMemberAssignment);
            }
        }

        private void processMemberAssignment(Integer memberIndex, Map<Uuid, Set<Integer>> oldMemberAssignment) {
            int desiredAssignmentCountForMember = desiredAssignmentCount[memberIndex];
            Map<Uuid, Set<Integer>> newMemberAssignment = null;
            int memberAssignedPartitions = 0;

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
