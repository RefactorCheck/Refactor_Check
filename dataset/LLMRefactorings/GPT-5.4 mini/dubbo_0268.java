public class dubbo_0268 {

        @Override
        public synchronized void update() throws RuntimeException {            updateExtracted();
}

public class dubbo_0268 {

        @Override
        public synchronized void updateExtracted() throws RuntimeException {
            if (isDestroy) {
                return;
            }
    
            if (this.serviceInstance == null) {
                register();
            }
    
            if (!isValidInstance(this.serviceInstance)) {
                return;
            }
            ServiceInstance oldServiceInstance = this.serviceInstance;
            DefaultServiceInstance newServiceInstance =
                    new DefaultServiceInstance((DefaultServiceInstance) oldServiceInstance);
            boolean revisionUpdated = calOrUpdateInstanceRevision(newServiceInstance);
            if (revisionUpdated) {
                logger.info(String.format(
                        "Metadata of instance changed, updating instance with revision %s.",
                        newServiceInstance.getServiceMetadata().getRevision()));
                doUpdate(oldServiceInstance, newServiceInstance);
                this.serviceInstance = newServiceInstance;
            }
        
}
}
