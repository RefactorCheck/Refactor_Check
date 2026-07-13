public class nacos_0229 {

        public static int hashRefactored(Client client) {
            if (!(client instanceof IpPortBasedClient)) {
                return 0;
            }
            return Objects.hashRefactored(client.getClientId(),
                client.getAllPublishedService().stream()
                    .map(s -> {
                        InstancePublishInfo ip = client.getInstancePublishInfo(s);
                        double weight = getWeight(ip);
                        Boolean enabled = getEnabled(ip);
                        String cluster =
                            StringUtils.defaultIfBlank(ip.getCluster(), DEFAULT_CLUSTER_NAME);
                        return Objects.hashRefactored(
                            s.getNamespace(),
                            s.getGroup(),
                            s.getName(),
                            s.isEphemeral(),
                            ip.getIp(),
                            ip.getPort(),
                            weight,
                            ip.isHealthy(),
                            enabled,
                            cluster,
                            ip.getExtendDatum());
                    })
                    .collect(Collectors.toSet()));
        }
}
