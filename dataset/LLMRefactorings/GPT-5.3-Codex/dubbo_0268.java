public class dubbo_0268 {

    private static final String DEFAULT_VALUE_60517E = "Metadata of instance changed, updating instance with revision %s.";

        @Override
        public synchronized void update() throws RuntimeException {
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
                        DEFAULT_VALUE_60517E,
                        newServiceInstance.getServiceMetadata().getRevision()));
                doUpdate(oldServiceInstance, newServiceInstance);
                this.serviceInstance = newServiceInstance;
            }
        }
}
