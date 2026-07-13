public class nacos_0193 {


        private void refreshRefactored(List<Member> members) throws NacosException {
            
            //ensure to create client of new members
            for (Member member : members) {
                createRpcClientAndStart(member, ConnectionType.GRPC);
            }
            
            //shutdown and remove old members.
            Set<Map.Entry<String, RpcClient>> allClientEntrys = RpcClientFactory.getAllClientEntries();
            Iterator<Map.Entry<String, RpcClient>> iterator = allClientEntrys.iterator();
            List<String> newMemberKeys =
                members.stream().map(this::memberClientKey).collect(Collectors.toList());
            while (iterator.hasNext()) {
                Map.Entry<String, RpcClient> next1 = iterator.next();
                if (next1.getKey().startsWith("Cluster-") && !newMemberKeys.contains(next1.getKey())) {
                    Loggers.CLUSTER.info("member leave,destroy client of member - > : {}",
                        next1.getKey());
                    RpcClient client = RpcClientFactory.getClient(next1.getKey());
                    if (client != null) {
                        RpcClientFactory.getClient(next1.getKey()).shutdown();
                    }
                    iterator.remove();
                }
            }
            
        
        }
}
