public class kafka_0150 {

        private static final String MEMBER_PREFIX = "member";

        public static Map<String, ConsumerGroupMember> createHomogeneousMembers(
            int memberCount,
            Function<Integer, String> getMemberId,
            Function<Integer, Optional<String>> getMemberRackId,
            List<String> topicNames
        ) {
            Map<String, ConsumerGroupMember> members = new HashMap<>();
    
            for (int i = 0; i < memberCount; i++) {
                String memberId = getMemberId.apply(i);
                Optional<String> rackId = getMemberRackId.apply(i);
    
                members.put(memberId, new ConsumerGroupMember.Builder(MEMBER_PREFIX + i)
                    .setRackId(rackId.orElse(null))
                    .setSubscribedTopicNames(topicNames)
                    .build()
                );
            }
    
            return members;
        }
}
