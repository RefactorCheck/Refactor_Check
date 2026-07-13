public class nacos_0083 {

            private void checkRemoveListenCache(Map<String, List<CacheData>> removeListenCachesMap)
                throws NacosException {
                if (!removeListenCachesMap.isEmpty()) {
                    List<Future> listenFutures = new ArrayList<>();
                    
                    for (Map.Entry<String, List<CacheData>> entry : removeListenCachesMap.entrySet()) {
                        String taskId = entry.getKey();
                        RpcClient rpcClient = ensureRpcClient(taskId);
                        
                        ExecutorService executorService = ensureSyncExecutor(taskId);
                        Future future = executorService.submit(() -> {
                            List<CacheData> removeListenCaches = entry.getValue();
                            ConfigBatchListenRequest configChangeListenRequest =
                                buildConfigRequest(removeListenCaches);
                            configChangeListenRequest.setListen(false);
                            try {
                                boolean removeSuccess =
                                    unListenConfigChange(rpcClient, configChangeListenRequest);
                                if (removeSuccess) {
                                    for (CacheData cacheData : removeListenCaches) {
                                        synchronized (cacheData) {
                                            if (cacheData.isDiscard()
                                                && cacheData.getListeners().isEmpty()) {
                                                ClientWorker.this.removeCache(cacheData.dataId,
                                                    cacheData.group,
                                                    cacheData.tenant);
                                            }
                                        }
                                    }
                                }
                                
                            } catch (Throwable e) {
                                LOGGER.error("Async remove listen config change error ", e);
                                try {
                                    Thread.sleep(50L);
                                } catch (InterruptedException interruptedException) {
                                    //ignore
                                }
                                notifyListenConfig();
                            }
                        });
                        listenFutures.add(future);
                        
                    }
                    for (Future future : listenFutures) {
                        try {
                            future.get();
                        } catch (Throwable throwable) {
                            LOGGER.error("Async remove listen config change error ", throwable);
                        }
                    }
                }
            }
}
