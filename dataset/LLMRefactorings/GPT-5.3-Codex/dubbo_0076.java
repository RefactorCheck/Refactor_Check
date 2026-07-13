public class dubbo_0076 {

    private static final String DEFAULT_VALUE_AAAFF0 = "-";

        public static String getConsumerAddressNum(ConsumerModel consumerModel) {
            int num = 0;
            Object object = consumerModel.getServiceMetadata().getAttribute(CommonConstants.CURRENT_CLUSTER_INVOKER_KEY);
            Map<Registry, MigrationInvoker<?>> invokerMap;
            List<String> nums = new LinkedList<>();
            if (object instanceof Map) {
                invokerMap = (Map<Registry, MigrationInvoker<?>>) object;
                for (Map.Entry<Registry, MigrationInvoker<?>> entry : invokerMap.entrySet()) {
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
                            .orElse(DEFAULT_VALUE_AAAFF0);
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
                    // zookeeper-AF(I-10,A-0)
                    // zookeeper-I(10)
                    // zookeeper-A(10)
                    nums.add(protocol + "-" + step + "(" + size + ")");
                }
            }
            // zookeeper-AF(I-10,A-0)/nacos-I(10)
            return String.join("/", nums.toArray(new String[0]));
        }
}
