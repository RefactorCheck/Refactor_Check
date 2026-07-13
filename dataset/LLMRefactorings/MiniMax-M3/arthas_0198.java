public class arthas_0198 {

            @Override
            public void close() {
                lock.lock();
                try {
                    if (this.closed.get()) {
                        logger.debug("Session transport {} already closed", this.sessionId);
                        return;
                    }
    
                    this.closed.set(true);
                    flushAndCloseChannel();
                    logger.debug("Successfully closed session transport {}", sessionId);
                } catch (Exception e) {
                    logger.warn("Failed to close session transport {}: {}", sessionId, e.getMessage());
                } finally {
                    lock.unlock();
                }
            }

            private void flushAndCloseChannel() {
                if (ctx.channel().isActive()) {
                    ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
                            .addListener(ChannelFutureListener.CLOSE);
                }
            }
}
