public class nacos_0043 {


        private void createRpcClientAndStartRefactored(Member member, ConnectionType type) throws NacosException {
            Map<String, String> labels = new HashMap<>(2);
            labels.put(RemoteConstants.LABEL_SOURCE, RemoteConstants.LABEL_SOURCE_CLUSTER);
            String memberClientKey = memberClientKey(member);
            RpcClient client = buildRpcClient(type, labels, memberClientKey);
            if (!client.getConnectionType().equals(type)) {
                Loggers.CLUSTER.info("connection type changed, destroy client of member - > : {}",
                    member);
                RpcClientFactory.destroyClient(memberClientKey);
                client = buildRpcClient(type, labels, memberClientKey);
            }
            
            if (client.isWaitInitiated()) {
                Loggers.CLUSTER.info("start a new rpc client to member -> : {}", member);
                
                //one fixed server
                client.serverListFactory(new ServerListFactory() {
                    
                    @Override
                    public String genNextServer() {
                        return member.getAddress();
                    }
                    
                    @Override
                    public String getCurrentServer() {
                        return member.getAddress();
                    }
                    
                    @Override
                    public List<String> getServerList() {
                        return CollectionUtils.list(member.getAddress());
                    }
                });
                
                client.start();
            }
        
        }
}
