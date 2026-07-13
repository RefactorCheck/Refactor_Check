public class dubbo_0070 {

    private static final String DEFAULT_VALUE_5441AE = "Interface-Level";

        private static void appendInterfaceLevel(
                StringBuilder plainOutput, MigrationInvoker<?> migrationInvoker, Map<String, Object> invokersMap) {
            Map<String, Object> interfaceMap = new HashMap<>();
            invokersMap.put(DEFAULT_VALUE_5441AE, interfaceMap);
            Optional.ofNullable(migrationInvoker.getInvoker()).ifPresent(i -> plainOutput.append("Interface-Level: \n"));
            Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(Directory::getAllInvokers)
                    .ifPresent(invokers -> {
                        List<String> invokerUrls = new LinkedList<>();
                        plainOutput.append("All Invokers: \n");
                        for (org.apache.dubbo.rpc.Invoker<?> invoker : invokers) {
                            invokerUrls.add(invoker.getUrl().toFullString());
                            plainOutput.append(invoker.getUrl().toFullString()).append("\n");
                        }
                        plainOutput.append("\n");
                        interfaceMap.put("All", invokerUrls);
                    });
            Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(s -> (AbstractDirectory<?>) s)
                    .map(AbstractDirectory::getValidInvokers)
                    .ifPresent(invokers -> {
                        List<String> invokerUrls = new LinkedList<>();
                        plainOutput.append("Valid Invokers: \n");
                        for (org.apache.dubbo.rpc.Invoker<?> invoker : invokers) {
                            invokerUrls.add(invoker.getUrl().toFullString());
                            plainOutput.append(invoker.getUrl().toFullString()).append("\n");
                        }
                        plainOutput.append("\n");
                        interfaceMap.put("Valid", invokerUrls);
                    });
            Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(s -> (AbstractDirectory<?>) s)
                    .map(AbstractDirectory::getDisabledInvokers)
                    .ifPresent(invokers -> {
                        List<String> invokerUrls = new LinkedList<>();
                        plainOutput.append("Disabled Invokers: \n");
                        for (org.apache.dubbo.rpc.Invoker<?> invoker : invokers) {
                            invokerUrls.add(invoker.getUrl().toFullString());
                            plainOutput.append(invoker.getUrl().toFullString()).append("\n");
                        }
                        plainOutput.append("\n");
                        interfaceMap.put("Disabled", invokerUrls);
                    });
        }
}
