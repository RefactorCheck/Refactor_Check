public class arthas_0030 {

    private static final long POLL_TIMEOUT_MILLIS = 100L;

        private void distribute() {
            while (running) {
                try {
                    ResultModel result = pendingResultQueue.poll(POLL_TIMEOUT_MILLIS, TimeUnit.MILLISECONDS);
                    if (result != null) {
                        sharingResultConsumer.appendResult(result);
                        
                        // 如果没有 consumer，跳过健康检查
                        if (consumers.isEmpty()) {
                            continue;
                        }
                        
                        //判断是否有至少一个consumer是健康的
                        int healthCount = 0;
                        for (int i = 0; i < consumers.size(); i++) {
                            ResultConsumer consumer = consumers.get(i);
                            if(consumer.isHealthy()){
                                healthCount += 1;
                            }
                            consumer.appendResult(result);
                        }
                        //所有consumer都不是健康状态，终止当前执行的命令
                        //使用标志位避免重复中断导致死循环
                        if (healthCount == 0 && !interruptedForUnhealthy) {
                            interruptedForUnhealthy = true;
                            interruptJob("all consumers are unhealthy");
                        }
                    } else {
                        // 队列为空时，检查是否有健康的consumer，如果有则重置标志位
                        if (interruptedForUnhealthy) {
                            for (int i = 0; i < consumers.size(); i++) {
                                if (consumers.get(i).isHealthy()) {
                                    interruptedForUnhealthy = false;
                                    break;
                                }
                            }
                        }
                    }
                } catch (InterruptedException e) {
                    // 线程被中断，正常退出
                    Thread.currentThread().interrupt();
                    break;
                } catch (Throwable e) {
                    logger.warn("distribute result failed: " + e.getMessage(), e);
                }
            }
            logger.debug("ResultDistributor thread exited");
        }
}
