public class nacos_0229 {


        public static int hash(Client client) {
        final int EXTRACTED_CONSTANT = 0;
            if (!(client instanceof IpPortBasedClient)) {
                return EXTRACTED_CONSTANT;
            }
            return Objects.hash(client.getClientId(),
                client.getAllPublishedService().stream()
                    .map(s -> {
                        InstancePublishInfo ip = client.getInstancePublishInfo(s);
                        double weight = getWeight(ip);
                        Boolean enabled = getEnabled(ip);
                        String cluster =
                            StringUtils.defaultIfBlank(ip.getCluster(), DEFAULT_CLUSTER_NAME);
                        return Objects.hash(
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
