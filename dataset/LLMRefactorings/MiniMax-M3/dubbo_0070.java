public class dubbo_0070 {

        private static void appendInterfaceLevel(
                StringBuilder plainOutput, MigrationInvoker<?> migrationInvoker, Map<String, Object> invokersMap) {
            Map<String, Object> interfaceMap = new HashMap<>();
            invokersMap.put("Interface-Level", interfaceMap);
            Optional.ofNullable(migrationInvoker.getInvoker()).ifPresent(i -> plainOutput.append("Interface-Level: \n"));
            Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(Directory::getAllInvokers)
                    .ifPresent(invokers -> appendInvokerUrls(invokers, "All Invokers: \n", "All", plainOutput, interfaceMap));
            Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(s -> (AbstractDirectory<?>) s)
                    .map(AbstractDirectory::getValidInvokers)
                    .ifPresent(invokers -> appendInvokerUrls(invokers, "Valid Invokers: \n", "Valid", plainOutput, interfaceMap));
            Optional.ofNullable(migrationInvoker.getInvoker())
                    .map(ClusterInvoker::getDirectory)
                    .map(s -> (AbstractDirectory<?>) s)
                    .map(AbstractDirectory::getDisabledInvokers)
                    .ifPresent(invokers -> appendInvokerUrls(invokers, "Disabled Invokers: \n", "Disabled", plainOutput, interfaceMap));
        }

        private static void appendInvokerUrls(List<org.apache.dubbo.rpc.Invoker<?>> invokers, String header, String mapKey,
                StringBuilder plainOutput, Map<String, Object> interfaceMap) {
            List<String> invokerUrls = new LinkedList<>();
            plainOutput.append(header);
            for (org.apache.dubbo.rpc.Invoker<?> invoker : invokers) {
                invokerUrls.add(invoker.getUrl().toFullString());
                plainOutput.append(invoker.getUrl().toFullString()).append("\n");
            }
            plainOutput.append("\n");
            interfaceMap.put(mapKey, invokerUrls);
        }
}
