public class arthas_0098 {

    public CompletableFuture<Void> closeGracefully() {
        return CompletableFuture.runAsync(() -> {
            this.isClosing.set(true);
            logger.debug("Initiating graceful shutdown with {} active sessions", this.sessions.size());

            closeAllSessions();

            this.sessions.clear();
            logger.debug("Graceful shutdown completed");

            shutdownKeepAliveScheduler();
        });
    }

    private void closeAllSessions() {
        this.sessions.values().parallelStream().forEach(session -> {
            try {
                session.closeGracefully();
            } catch (Exception e) {
                logger.error("Failed to close session {}: {}", session.getId(), e.getMessage());
            }
        });
    }

    private void shutdownKeepAliveScheduler() {
        if (this.keepAliveScheduler != null) {
            this.keepAliveScheduler.shutdown();
        }
    }
}
