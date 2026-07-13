public class nacos_0052 {

            RpcClient ensureRpcClient(String taskId) throws NacosException {
                synchronized (ClientWorker.this) {
                    Map<String, String> labels = getLabels();
                    Map<String, String> newLabels = new HashMap<>(labels);
                    newLabels.put("taskId", taskId);
                    GrpcClientConfig grpcClientConfig = RpcClientConfigFactory.getInstance()
                        .createGrpcClientConfig(properties, newLabels);
                    RpcClient rpcClient = RpcClientFactory.createClient(uuid + "_config-" + taskId,
                        getConnectionType(),
                        grpcClientConfig);
                    if (rpcClient.isWaitInitiated()) {
                        initializeAndStartClient(rpcClient);
                    }
                    
                    return rpcClient;
                }
                
            }
            
            private void initializeAndStartClient(RpcClient rpcClient) {
                initRpcClientHandler(rpcClient);
                rpcClient.setTenant(getTenant());
                rpcClient.start();
            }
}
