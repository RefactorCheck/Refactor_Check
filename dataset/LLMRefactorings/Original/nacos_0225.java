public class nacos_0225 {

        @Override
        public List<ObjectNode> getSubscribeClientList(String namespaceId, String groupName,
            String serviceName,
            boolean ephemeral, String ip, Integer port) {
            Service service = Service.newService(namespaceId, groupName, serviceName, ephemeral);
            Collection<String> allClientsSubscribeService =
                clientServiceIndexesManager.getAllClientsSubscribeService(
                    service);
            ArrayList<ObjectNode> res = new ArrayList<>();
            for (String clientId : allClientsSubscribeService) {
                Client client = clientManager.getClient(clientId);
                Subscriber subscriber = client.getSubscriber(service);
                boolean ipMatch = Objects.isNull(ip) || Objects.equals(ip, subscriber.getIp());
                boolean portMatch = Objects.isNull(port) || Objects.equals(port, subscriber.getPort());
                if (!ipMatch || !portMatch) {
                    continue;
                }
                ObjectNode item = JacksonUtils.createEmptyJsonNode();
                item.put("clientId", clientId);
                item.put("ip", subscriber.getIp());
                item.put("port", subscriber.getPort());
                res.add(item);
            }
            
            return res;
        }
}
