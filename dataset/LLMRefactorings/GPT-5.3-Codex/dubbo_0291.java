public class dubbo_0291 {

        public boolean offline(String servicePattern) {
            boolean hasService = false;
    
            ExecutorService executorService = Executors.newFixedThreadPool(
                    Math.min(Runtime.getRuntime().availableProcessors(), 4), new NamedThreadFactory("Dubbo-Offline"));
            try {
                List<CompletableFuture<Void>> futures = new LinkedList<>();
                Collection<ProviderModel> providerModelList = serviceRepository.allProviderModels();
                for (ProviderModel providerModel : providerModelList) {
                    ServiceMetadata metadata = providerModel.getServiceMetadata();
                    if (metadata.getServiceKey().matches(servicePattern)
                            || metadata.getDisplayServiceKey().matches(servicePattern)) {
                        hasService = true;
                        List<ProviderModel.RegisterStatedURL> statedUrls = providerModel.getStatedUrl();
                        for (ProviderModel.RegisterStatedURL statedURL : statedUrls) {
                            if (statedURL.isRegistered()) {
                                futures.add(CompletableFuture.runAsync(
                                        () -> {
                                            doUnexport(statedURL);
                                        },
                                        executorService));
                            }
                        }
                    }
                }
                for (CompletableFuture<Void> future : futures) {
                    future.get();
                }
            } catch (ExecutionException | InterruptedException e) {
                throw new RuntimeException(e);
            } finally {
                executorService.shutdown();
            }
    
            boolean result = hasService;
    
            return result;}
}
