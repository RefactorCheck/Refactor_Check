public class nacos_0061 {

            private void updateSyncDataToClient(Map.Entry<String, ClientSyncData> entry,
                IpPortBasedClient client) {
                ClientSyncData data = entry.getValue();
                List<String> namespaces = data.getNamespaces();
                List<String> groupNames = data.getGroupNames();
                List<String> serviceNames = data.getServiceNames();
                List<InstancePublishInfo> instances = data.getInstancePublishInfos();
                // alive instance data: Service = InstancePublishInfo
                Map<Service, InstancePublishInfo> newInstanceInfoMap = new HashMap<>(instances.size());
                for (int i = 0; i < namespaces.size(); i++) {
                    Service service = Service.newService(namespaces.get(i), groupNames.get(i),
                        serviceNames.get(i), false);
                    newInstanceInfoMap.put(service, instances.get(i));
                }
                // old instance data
                Collection<Service> oldPublishedService = client.getAllPublishedService();
                Set<Service> aliveInstanceServices = newInstanceInfoMap.keySet();
                // add or update existed service
                for (Service service : aliveInstanceServices) {
                    Service singleton = ServiceManager.getInstance().getSingleton(service);
                    InstancePublishInfo newInstanceInfo = newInstanceInfoMap.get(singleton);
                    if (oldPublishedService.contains(singleton)) {
                        // update if necessary
                        InstancePublishInfo oldInstanceInfo = client.getInstancePublishInfo(singleton);
                        if (oldInstanceInfo != null && !newInstanceInfo.equals(oldInstanceInfo)) {
                            client.putServiceInstance(singleton, newInstanceInfo);
                            NotifyCenter.publishEvent(
                                new ClientOperationEvent.ClientRegisterServiceEvent(singleton,
                                    client.getClientId()));
                            Loggers.RAFT.info("[SNAPSHOT-DATA-UPDATE] service={}, instance={}", service,
                                newInstanceInfo);
                        }
                    } else {
                        // add
                        client.putServiceInstance(singleton, newInstanceInfo);
                        NotifyCenter.publishEvent(
                            new ClientOperationEvent.ClientRegisterServiceEvent(singleton,
                                client.getClientId()));
                        Loggers.RAFT.info("[SNAPSHOT-DATA-ADD] service={}, instance={}", service,
                            newInstanceInfo);
                    }
                }
                // remove dead instance
                for (Service service : oldPublishedService) {
                    if (!aliveInstanceServices.contains(service)) {
                        InstancePublishInfo oldInfo = client.getInstancePublishInfo(service);
                        // metric ip count decrement
                        client.removeServiceInstance(service);
                        NotifyCenter.publishEvent(
                            new ClientOperationEvent.ClientDeregisterServiceEvent(service,
                                client.getClientId()));
                        Loggers.RAFT.info("[SNAPSHOT-DATA-REMOVE] service={}, instance={}", service,
                            oldInfo);
                    }
                }
            }
}
