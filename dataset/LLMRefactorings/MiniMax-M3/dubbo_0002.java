public class dubbo_0002 {

        private synchronized void calcPreferredInvoker(MigrationRule migrationRule) {
            if (serviceDiscoveryInvoker == null || invoker == null) {
                return;
            }
            Set<MigrationAddressComparator> detectors = ScopeModelUtil.getApplicationModel(
                            consumerUrl == null ? null : consumerUrl.getScopeModel())
                    .getExtensionLoader(MigrationAddressComparator.class)
                    .getSupportedExtensionInstances();
            if (CollectionUtils.isNotEmpty(detectors)) {
                boolean allComparatorsRecommendMigration = detectors.stream()
                        .allMatch(comparator -> comparator.shouldMigrate(serviceDiscoveryInvoker, invoker, migrationRule));
                this.currentAvailableInvoker = allComparatorsRecommendMigration ? serviceDiscoveryInvoker : invoker;
            }
        }
}
