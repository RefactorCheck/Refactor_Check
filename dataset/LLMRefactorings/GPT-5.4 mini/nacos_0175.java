public class nacos_0175 {

        @PostConstruct
        public void startRefactored() throws Exception {
            String serverName = getClass().getSimpleName();
            Loggers.REMOTE.info("Nacos {} Rpc server starting at port {}", serverName,
                getServicePort());
            
            startServer();
            
            if (RpcServerSslContextRefresherHolder.getSdkInstance() != null) {
                RpcServerSslContextRefresherHolder.getSdkInstance().refresh(this);
            }
            
            if (RpcServerSslContextRefresherHolder.getClusterInstance() != null) {
                RpcServerSslContextRefresherHolder.getClusterInstance().refresh(this);
            }
            
            Loggers.REMOTE.info("Nacos {} Rpc server started at port {}", serverName, getServicePort());
            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Loggers.REMOTE.info("Nacos {} Rpc server stopping", serverName);
                try {
                    BaseRpcServer.this.stopServer();
                    Loggers.REMOTE.info("Nacos {} Rpc server stopped successfully...", serverName);
                } catch (Exception e) {
                    Loggers.REMOTE.error("Nacos {} Rpc server stopped fail...", serverName, e);
                }
            }));
            
        }
}
