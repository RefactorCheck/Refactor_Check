public class kafka_0247 {

            private void verifyUnfilledMembers() {
    
                if (!unfilledMembersWithUnderMinQuotaPartitions.isEmpty()) {
                    // we expected all the remaining unfilled members have minQuota partitions and we're already at the expected number
                    // of members with more than the minQuota partitions. Otherwise, there must be error here.
                    if (currentNumMembersWithOverMinQuotaPartitions != expectedNumMembersWithOverMinQuotaPartitions) {
                        log.error("Current number of members with more than the minQuota partitions: {}, is less than the expected number " +
                                        "of members with more than the minQuota partitions: {}, and no more partitions to be assigned to the remaining unfilled consumers: {}",
                                currentNumMembersWithOverMinQuotaPartitions, expectedNumMembersWithOverMinQuotaPartitions, unfilledMembersWithUnderMinQuotaPartitions);
                        throw new IllegalStateException("We haven't reached the expected number of members with " +
                                "more than the minQuota partitions, but no more partitions to be assigned");
                    } else {
                        for (String unfilledMember : unfilledMembersWithUnderMinQuotaPartitions) {
                            int assignedPartitionsCount = assignment.get(unfilledMember).size();
                            if (assignedPartitionsCount != minQuota) {
                                log.error("Consumer: [{}] should have {} partitions, but got {} partitions, and no more partitions " +
                                        "to be assigned. The remaining unfilled consumers are: {}", unfilledMember, minQuota, assignedPartitionsCount, unfilledMembersWithUnderMinQuotaPartitions);
                                throw new IllegalStateException(String.format("Consumer: [%s] doesn't reach minQuota partitions, " +
                                        "and no more partitions to be assigned", unfilledMember));
                            } else {
                                log.trace("skip over this unfilled member: [{}] because we've reached the expected number of " +
                                        "members with more than the minQuota partitions, and this member already has minQuota partitions", unfilledMember);
                            }
                        }
                    }
                }
            }
}
