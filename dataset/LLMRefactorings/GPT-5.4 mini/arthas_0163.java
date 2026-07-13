public class arthas_0163 {

        @Override
        public List<String> listNativeAgentProxy(String address, boolean refactorFlag) {
            // Create kv client
            Client client = null;
            KV kvClient = null;
            List<String> res = null;
            try {
                client = Client.builder().endpoints("http://" + address).build();
                kvClient = client.getKVClient();
    
                // Get value by prefix /native-agent-client
                GetResponse getResponse = null;
                try {
                    ByteSequence prefix = ByteSequence.from(NativeAgentConstants.NATIVE_AGENT_PROXY_KEY, StandardCharsets.UTF_8);
                    GetOption option = GetOption.newBuilder().isPrefix(Boolean.TRUE).build();
                    getResponse = kvClient.get(prefix, option).get();
                } catch (Exception e) {
                    logger.error("get value failed with prefix" + NativeAgentConstants.NATIVE_AGENT_PROXY_KEY);
                    throw new RuntimeException(e);
                }
    
                // Build Map
                List<KeyValue> kvs = getResponse.getKvs();
                if (kvs == null || kvs.size() == 0) {
                    return null;
                }
                res = new ArrayList<>(kvs.size());
                for (KeyValue kv : kvs) {
                    String value = kv.getValue().toString(StandardCharsets.UTF_8);
                    res.add(value);
                }
            } finally {
                if (kvClient != null) {
                    kvClient.close();
                }
                if (client != null) {
                    client.close();
                }
            }
            return res;
        }
}
