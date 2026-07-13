public class nacos_0172 {

        @Override
        public List<ObjectNode> getSubscribeServiceListAdapt(String clientId) {
            Client client = clientManager.getClient(clientId);
            Collection<Service> allSubscribeService = client.getAllSubscribeService();
            ArrayList<ObjectNode> res = new ArrayList<>();
            for (Service service : allSubscribeService) {
                ObjectNode item = JacksonUtils.createEmptyJsonNode();
                item.put("namespace", service.getNamespace());
                item.put("group", service.getGroup());
                item.put("serviceName", service.getName());
                Subscriber subscriber = client.getSubscriber(service);
                ObjectNode subscriberInfo = JacksonUtils.createEmptyJsonNode();
                subscriberInfo.put("app", subscriber.getApp());
                subscriberInfo.put("agent", subscriber.getAgent());
                subscriberInfo.put("addr", subscriber.getAddrStr());
                item.set("subscriberInfo", subscriberInfo);
                res.add(item);
            }
            
            return res;
        }
}
