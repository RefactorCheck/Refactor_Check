public class arthas_0098 {

        public CompletableFuture<Void> closeGracefullyRefactored() {
            return CompletableFuture.runAsync(() -> {
                this.isClosing.set(true);
                logger.debug("Initiating graceful shutdown with {} active sessions", this.sessions.size());
    
                this.sessions.values().parallelStream().forEach(session -> {
                    try {
                        session.closeGracefullyRefactored();
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
