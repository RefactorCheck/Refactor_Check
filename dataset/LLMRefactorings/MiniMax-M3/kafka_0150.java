public class kafka_0150 {

    public static Map<String, ConsumerGroupMember> createHomogeneousMembers(
        int memberCount,
        Function<Integer, String> getMemberId,
        Function<Integer, Optional<String>> getMemberRackId,
        List<String> topicNames
    ) {
        Map<String, ConsumerGroupMember> members = new HashMap<>();

        for (int i = 0; i < memberCount; i++) {
            members.put(getMemberId.apply(i), buildMember(i, getMemberRackId.apply(i), topicNames));
        }

        return members;
    }

    private static ConsumerGroupMember buildMember(int index, Optional<String> rackId, List<String> topicNames) {
        return new ConsumerGroupMember.Builder("member" + index)
            .setRackId(rackId.orElse(null))
            .setSubscribedTopicNames(topicNames)
            .build();
    }
}
