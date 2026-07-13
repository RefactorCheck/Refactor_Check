public class nacos_0112 {

    private void upgradeClient(Client client, ClientSyncData clientSyncData) {
        Set<Service> syncedService = new HashSet<>();
        processBatchInstanceDistroData(syncedService, client, clientSyncData);
        syncInstanceServices(syncedService, client, clientSyncData);
        removeUnsyncedServices(syncedService, client);
        client.setRevision(clientSyncData.getAttributes()
            .<Integer>getClientAttribute(ClientConstants.REVISION, 0));
    }

    private void syncInstanceServices(Set<Service> syncedService, Client client,
            ClientSyncData clientSyncData) {
        List<String> namespaces = clientSyncData.getNamespaces();
        List<String> groupNames = clientSyncData.getGroupNames();
        List<String> serviceNames = clientSyncData.getServiceNames();
        List<InstancePublishInfo> instances = clientSyncData.getInstancePublishInfos();

        for (int i = 0; i < namespaces.size(); i++) {
            Service service =
                Service.newService(namespaces.get(i), groupNames.get(i), serviceNames.get(i));
            Service singleton = ServiceManager.getInstance().getSingleton(service);
            syncedService.add(singleton);
            InstancePublishInfo instancePublishInfo = instances.get(i);
            if (!instancePublishInfo.equals(client.getInstancePublishInfo(singleton))) {
                client.addServiceInstance(singleton, instancePublishInfo);
                NotifyCenter.publishEvent(
                    new ClientOperationEvent.ClientRegisterServiceEvent(singleton,
                        client.getClientId()));
                NotifyCenter.publishEvent(
                    new MetadataEvent.InstanceMetadataEvent(singleton,
                        instancePublishInfo.getMetadataId(), false));
            }
        }
    }

    private void removeUnsyncedServices(Set<Service> syncedService, Client client) {
        for (Service each : client.getAllPublishedService()) {
            if (!syncedService.contains(each)) {
                client.removeServiceInstance(each);
                NotifyCenter.publishEvent(
                    new ClientOperationEvent.ClientDeregisterServiceEvent(each,
                        client.getClientId()));
            }
        }
    }
}
