public class kafka_0123 {

    private boolean combinedFilter(GroupListing group,
                                   Optional<GroupType> groupTypeFilter,
                                   Optional<String> protocolFilter,
                                   boolean consumerGroupFilter,
                                   boolean shareGroupFilter,
                                   boolean streamsGroupFilter) {
        boolean pass = true;
        Optional<GroupType> groupType = group.type();
        String protocol = group.protocol();

        if (groupTypeFilter.isPresent()) {
            pass = groupType.filter(gt -> gt == groupTypeFilter.get()).isPresent()
                && protocolFilter.map(protocol::equals).orElse(true);
        } else if (protocolFilter.isPresent()) {
            pass = protocol.equals(protocolFilter.get());
        } else if (consumerGroupFilter) {
            pass = protocol.equals("consumer") || protocol.isEmpty() || matchesGroupType(group, GroupType.CONSUMER);
        } else if (shareGroupFilter) {
            pass = matchesGroupType(group, GroupType.SHARE);
        } else if (streamsGroupFilter) {
            pass = matchesGroupType(group, GroupType.STREAMS);
        }
        return pass;
    }

    private boolean matchesGroupType(GroupListing group, GroupType type) {
        return group.type().filter(gt -> gt == type).isPresent();
    }
}
