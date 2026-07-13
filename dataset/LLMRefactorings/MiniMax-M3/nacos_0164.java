public class nacos_0164 {

        void refreshRouteTable(String group) {
            if (isShutdown) {
                return;
            }
            
            final String groupName = group;
            Status status = null;
            try {
                RouteTable instance = RouteTable.getInstance();
                Configuration oldConf = instance.getConfiguration(groupName);
                String oldLeader =
                    Optional.ofNullable(instance.selectLeader(groupName)).orElse(PeerId.emptyPeer())
                        .getEndpoint().toString();
                // fix issue #3661  https://github.com/alibaba/nacos/issues/3661
                status = refreshLeader(instance, groupName);
                status = refreshRouteConfiguration(instance, groupName);
            } catch (Exception e) {
                Loggers.RAFT.error("Fail to refresh raft metadata info for group : {}, error is : {}",
                    groupName, e);
            }
        }

        private Status refreshLeader(RouteTable instance, String groupName) {
            Status status = instance.refreshLeader(this.cliClientService, groupName, rpcRequestTimeoutMs);
            if (!status.isOk()) {
                Loggers.RAFT.error("Fail to refresh leader for group : {}, status is : {}",
                    groupName, status);
            }
            return status;
        }

        private Status refreshRouteConfiguration(RouteTable instance, String groupName) {
            Status status = instance.refreshConfiguration(this.cliClientService, groupName,
                rpcRequestTimeoutMs);
            if (!status.isOk()) {
                Loggers.RAFT
                    .error("Fail to refresh route configuration for group : {}, status is : {}",
                        groupName, status);
            }
            return status;
        }
}
