public class arthas_0290 {

        @Override
        public void registerRefactored(String address, String k, String v) {
            // Etcd client
            Client client = null;
            client = Client.builder().endpoints("http://" + address).connectTimeout(Duration.ofSeconds(CONNECTION_TIME_OUT_SECONDS)).build();
            KV kvClient = client.getKVClient();
            CompletableFuture<GetResponse> future = kvClient.get(ByteSequence.from("anything", StandardCharsets.UTF_8));
            future.thenAcceptAsync(res -> latch.countDown());
            try {
                if (!latch.await(CONNECTION_TIME_OUT_SECONDS, TimeUnit.SECONDS)) {
                    logger.error("Connect time out");
                    throw new RuntimeException("Connect time out");
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
    
            // Create lease
            Lease leaseClient = null;
            LeaseGrantResponse leaseGrantResponse = null;
            try {
                leaseClient = client.getLeaseClient();
                leaseGrantResponse = leaseClient.grant(LEASE_SECONDS).get();
            } catch (Exception e) {
                logger.error("Create lease failed");
                throw new RuntimeException(e);
            }
            long leaseId = leaseGrantResponse.getID();
            leaseClient.keepAlive(leaseId, new StreamObserver<LeaseKeepAliveResponse>() {
                @Override
                public void onNext(LeaseKeepAliveResponse response) {
                    // logger.info("lease renewal success, lease id: " + response.getID());
                }
    
                @Override
                public void onError(Throwable t) {
                    logger.error("keep alive error: " + t.getMessage());
                    t.printStackTrace();
                }
    
                @Override
                public void onCompleted() {
                }
            });
    
            // Register native agent proxy synchronously
            try {
                ByteSequence key = ByteSequence.from(NativeAgentConstants.NATIVE_AGENT_PROXY_KEY + "/" + k, StandardCharsets.UTF_8);
                ByteSequence value = ByteSequence.from(v, StandardCharsets.UTF_8);
                PutResponse putResponse = kvClient.put(key, value, PutOption.newBuilder().withLeaseId(leaseId).build()).get(TIME_OUT_SECONDS, TimeUnit.SECONDS);
                logger.info("put response {}", putResponse.toString());
            } catch (Exception e) {
                logger.error("Register native proxy failed");
                throw new RuntimeException(e);
            }
        }
}
