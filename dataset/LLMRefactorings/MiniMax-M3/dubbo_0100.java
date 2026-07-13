public class dubbo_0100 {

        @Override
        protected void doUpdate(ServiceInstance oldServiceInstance, ServiceInstance newServiceInstance)
                throws RuntimeException {
            if (EMPTY_REVISION.equals(getExportedServicesRevision(newServiceInstance))
                    || EMPTY_REVISION.equals(
                            oldServiceInstance.getMetadata().get(EXPORTED_SERVICES_REVISION_PROPERTY_NAME))) {
                super.doUpdate(oldServiceInstance, newServiceInstance);
                return;
            }
    
            org.apache.curator.x.discovery.ServiceInstance<ZookeeperInstance> oldInstance = build(oldServiceInstance);
            org.apache.curator.x.discovery.ServiceInstance<ZookeeperInstance> newInstance = build(newServiceInstance);
            if (hasInstanceIdentityChanged(oldInstance, newInstance)) {
                super.doUpdate(oldServiceInstance, newServiceInstance);
                return;
            }
    
            try {
                this.serviceInstance = newServiceInstance;
                reportMetadata(newServiceInstance.getServiceMetadata());
                serviceDiscovery.updateService(newInstance);
            } catch (Exception e) {
                throw new RpcException(REGISTRY_EXCEPTION, "Failed register instance " + newServiceInstance.toString(), e);
            }
        }
    
        private boolean hasInstanceIdentityChanged(org.apache.curator.x.discovery.ServiceInstance<ZookeeperInstance> oldInstance,
                                                 org.apache.curator.x.discovery.ServiceInstance<ZookeeperInstance> newInstance) {
            return !Objects.equals(newInstance.getName(), oldInstance.getName())
                    || !Objects.equals(newInstance.getId(), oldInstance.getId());
        }
}
