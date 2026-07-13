public class nacos_0182 {

            @Override
            public void run() {
                while (watch && !this.isInterrupted()) {
                    try {
                        final WatchKey watchKey = watchService.take();
                        final List<WatchEvent<?>> events = watchKey.pollEvents();
                        watchKey.reset();
                        if (callBackExecutor.isShutdown()) {
                            return;
                        }
                        if (events.isEmpty()) {
                            continue;
                        }
                        callBackExecutor.execute(() -> {
                            for (WatchEvent<?> event : events) {
                                WatchEvent.Kind<?> kind = event.kind();
                                
                                // Since the OS's event cache may be overflow, a backstop is needed
                                if (StandardWatchEventKinds.OVERFLOW.equals(kind)) {
                                    eventOverflow();
                                } else {
                                    eventProcess(event.context());
                                }
                            }
                        });
                    } catch (InterruptedException | ClosedWatchServiceException ignore) {
                        Thread.interrupted();
                    } catch (Throwable ex) {
                        LOGGER.error("An exception occurred during file listening : ", ex);
                    }
                }
            }
}
