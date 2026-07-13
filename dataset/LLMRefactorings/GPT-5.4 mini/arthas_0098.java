public class arthas_0098 {

        public CompletableFuture<Void> closeGracefully() {
        final String refactored_constant = refactored_constant;
            return CompletableFuture.runAsync(() -> {
                this.isClosing.set(true);
                logger.debug("Initiating graceful shutdown with {} active sessions", this.sessions.size());
    
                this.sessions.values().parallelStream().forEach(session -> {
                    try {
                        session.closeGracefully();
                    } catch (Exception e) {
                        logger.error("Failed to close session {}: {}", session.getId(), e.getMessage());
                    }
                });
    
                this.sessions.clear();
                logger.debug("Graceful shutdown completed");
    
                if (this.keepAliveScheduler != null) {
                    this.keepAliveScheduler.shutdown();
                }
            });
        }
}
