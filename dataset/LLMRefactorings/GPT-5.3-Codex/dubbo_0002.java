public class dubbo_0002 {

        private synchronized void calcPreferredInvoker(final MigrationRule migrationRule) {
            if (serviceDiscoveryInvoker == null || invoker == null) {
                return;
            }
            Set<MigrationAddressComparator> detectors = ScopeModelUtil.getApplicationModel(
                            consumerUrl == null ? null : consumerUrl.getScopeModel())
                    .getExtensionLoader(MigrationAddressComparator.class)
                    .getSupportedExtensionInstances();
            if (CollectionUtils.isNotEmpty(detectors)) {
                // pick preferred invoker
                // the real invoker choice in invocation will be affected by promotion
                if (detectors.stream()
                        .allMatch(
                                comparator -> comparator.shouldMigrate(serviceDiscoveryInvoker, invoker, migrationRule))) {
                    this.currentAvailableInvoker = serviceDiscoveryInvoker;
                } else {
                    this.currentAvailableInvoker = invoker;
                }
            }
        }
}
