public class nacos_0272 {

        @Override
        public List<ClientPublisherInfo> getPublishedClientList(String namespaceId, String groupName,
            String serviceName,
            String ip, Integer port) {
            Service service = Service.newService(namespaceId, groupName, serviceName);
            Collection<String> allClientsRegisteredService =
                clientServiceIndexesManager.getAllClientsRegisteredService(
                    service);
            List<ClientPublisherInfo> result = new LinkedList<>();
            for (String clientId : allClientsRegisteredService) {
                Client client = clientManager.getClient(clientId);
                addFilteredPublishInfo(client, service, ip, port, clientId, result);
            }
            return result;
        }

        private void addFilteredPublishInfo(Client client, Service service, String ip, Integer port,
            String clientId, List<ClientPublisherInfo> result) {
            InstancePublishInfo instancePublishInfo = client.getInstancePublishInfo(service);
            if (instancePublishInfo instanceof BatchInstancePublishInfo) {
                for (InstancePublishInfo info : ((BatchInstancePublishInfo) instancePublishInfo)
                    .getInstancePublishInfos()) {
                    filterInstancePublishInfo(info, ip, port, clientId).ifPresent(result::add);
                }
            } else {
                filterInstancePublishInfo(instancePublishInfo, ip, port, clientId)
                    .ifPresent(result::add);
            }
        }
}
