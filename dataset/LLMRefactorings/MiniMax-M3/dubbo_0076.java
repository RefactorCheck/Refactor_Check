public class dubbo_0076 {

        public static String getConsumerAddressNum(ConsumerModel consumerModel) {
            Object object = consumerModel.getServiceMetadata().getAttribute(CommonConstants.CURRENT_CLUSTER_INVOKER_KEY);
            List<String> nums = new LinkedList<>();
            if (object instanceof Map) {
                Map<Registry, MigrationInvoker<?>> invokerMap = (Map<Registry, MigrationInvoker<?>>) object;
                for (Map.Entry<Registry, MigrationInvoker<?>> entry : invokerMap.entrySet()) {
                    nums.add(buildAddressString(entry));
                }
            }
            return String.join("/", nums.toArray(new String[0]));
        }

        private static String buildAddressString(Map.Entry<Registry, MigrationInvoker<?>> entry) {
            URL registryUrl = entry.getKey().getUrl();
            boolean isServiceDiscovery = UrlUtils.isServiceDiscoveryURL(registryUrl);
            String protocol = isServiceDiscovery
                    ? registryUrl.getParameter(RegistryConstants.REGISTRY_KEY)
                    : registryUrl.getProtocol();
            MigrationInvoker<?> migrationInvoker = entry.getValue();
            MigrationStep migrationStep = migrationInvoker.getMigrationStep();
            String interfaceSize = Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(Directory::getAllInvokers)
                    .map(List::size)
                    .map(String::valueOf)
                    .orElse("-");
            String applicationSize = Optional.ofNullable(migrationInvoker.getServiceDiscoveryInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(Directory::getAllInvokers)
                    .map(List::size)
                    .map(String::valueOf)
                    .orElse("-");
            String step;
            String size;
            switch (migrationStep) {
                case APPLICATION_FIRST:
                    step = "AF";
                    size = "I-" + interfaceSize + ",A-" + applicationSize;
                    break;
                case FORCE_INTERFACE:
                    step = "I";
                    size = interfaceSize;
                    break;
                default:
                    step = "A";
                    size = applicationSize;
                    break;
            }
            return protocol + "-" + step + "(" + size + ")";
        }
}
