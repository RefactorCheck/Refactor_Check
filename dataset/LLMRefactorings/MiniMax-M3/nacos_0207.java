public class nacos_0207 {

        @Override
        public void deregisterInstance(Service service, Instance instance, String clientId) {
            if (!ServiceManager.getInstance().containSingleton(service)) {
                Loggers.SRV_LOG.warn("remove instance from non-exist service: {}", service);
                return;
            }
            Service singleton = ServiceManager.getInstance().getSingleton(service);
            Client client = clientManager.getClient(clientId);
            checkClientIsLegal(client, clientId);
            InstancePublishInfo removedInstance = client.removeServiceInstance(singleton);
            client.setLastUpdatedTime();
            client.recalculateRevision();
            publishDeregistrationEvents(singleton, clientId, removedInstance);
        }

        private void publishDeregistrationEvents(Service singleton, String clientId, InstancePublishInfo removedInstance) {
            if (null != removedInstance) {
                NotifyCenter.publishEvent(
                    new ClientOperationEvent.ClientDeregisterServiceEvent(singleton, clientId));
                NotifyCenter.publishEvent(
                    new MetadataEvent.InstanceMetadataEvent(singleton, removedInstance.getMetadataId(),
                        true));
            }
        }
}
