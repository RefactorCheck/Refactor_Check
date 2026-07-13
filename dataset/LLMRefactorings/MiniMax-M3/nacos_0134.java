public class nacos_0134 {

        @Override
        public List<ClientSubscriberInfo> getSubscribeClientList(String namespaceId, String groupName,
            String serviceName,
            String ip, Integer port) {
            Service service = Service.newService(namespaceId, groupName, serviceName);
            Collection<String> allClientsSubscribeService =
                clientServiceIndexesManager.getAllClientsSubscribeService(
                    service);
            List<ClientSubscriberInfo> result = new LinkedList<>();
            for (String clientId : allClientsSubscribeService) {
                Client client = clientManager.getClient(clientId);
                Subscriber subscriber = client.getSubscriber(service);
                if (!matchesFilter(subscriber, ip, port)) {
                    continue;
                }
                ClientSubscriberInfo item = new ClientSubscriberInfo();
                item.setClientId(clientId);
                item.setAddress(subscriber.getAddrStr());
                item.setAgent(subscriber.getAgent());
                item.setAppName(subscriber.getApp());
                result.add(item);
            }
            return result;
        }

        private boolean matchesFilter(Subscriber subscriber, String ip, Integer port) {
            boolean ipMatch = Objects.isNull(ip) || Objects.equals(ip, subscriber.getIp());
            boolean portMatch = Objects.isNull(port) || Objects.equals(port, subscriber.getPort());
            return ipMatch && portMatch;
        }
}
