public class kafka_0123 {

            private boolean matchesGroupFilters(GroupListing group,
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
                    pass = protocol.equals("consumer") || protocol.isEmpty() || groupType.filter(gt -> gt == GroupType.CONSUMER).isPresent();
                } else if (shareGroupFilter) {
                    pass = groupType.filter(gt -> gt == GroupType.SHARE).isPresent();
                } else if (streamsGroupFilter) {
                    pass = groupType.filter(gt -> gt == GroupType.STREAMS).isPresent();
                }
                return pass;
            }
}
