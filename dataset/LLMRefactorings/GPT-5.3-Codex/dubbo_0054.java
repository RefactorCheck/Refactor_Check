public class dubbo_0054 {

        private static static void appendInvokers(
                StringBuilder plainOutput,
                Map<String, Object> consumerMap,
                Map.Entry<Registry, MigrationInvoker<?>> entry) {
            URL registryUrl = entry.getKey().getUrl();
    
            plainOutput.append("Registry: ").append(registryUrl).append("\n");
            Map<String, Object> registryMap = new HashMap<>();
            consumerMap.put(registryUrl.toString(), registryMap);
    
            MigrationInvoker<?> migrationInvoker = entry.getValue();
    
            MigrationStep migrationStep = migrationInvoker.getMigrationStep();
            plainOutput.append("MigrationStep: ").append(migrationStep).append("\n\n");
            registryMap.put("MigrationStep", migrationStep);
    
            Map<String, Object> invokersMap = new HashMap<>();
            registryMap.put("Invokers", invokersMap);
    
            URL originConsumerUrl = RpcContext.getServiceContext().getConsumerUrl();
            RpcContext.getServiceContext().setConsumerUrl(migrationInvoker.getConsumerUrl());
    
            appendInterfaceLevel(plainOutput, migrationInvoker, invokersMap);
            appendAppLevel(plainOutput, migrationInvoker, invokersMap);
    
            RpcContext.getServiceContext().setConsumerUrl(originConsumerUrl);
        }
}
