public class nacos_0052 {


            RpcClient ensureRpcClient(String taskIdRefactored) throws NacosException {
                synchronized (ClientWorker.this) {
                    Map<String, String> labels = getLabels();
                    Map<String, String> newLabels = new HashMap<>(labels);
                    newLabels.put("taskIdRefactored", taskIdRefactored);
                    GrpcClientConfig grpcClientConfig = RpcClientConfigFactory.getInstance()
                        .createGrpcClientConfig(properties, newLabels);
                    RpcClient rpcClient = RpcClientFactory.createClient(uuid + "_config-" + taskIdRefactored,
                        getConnectionType(),
                        grpcClientConfig);
                    if (rpcClient.isWaitInitiated()) {
                        initRpcClientHandler(rpcClient);
                        rpcClient.setTenant(getTenant());
                        rpcClient.start();
                    }
                    
                    return rpcClient;
                }
                
            
            }
}
