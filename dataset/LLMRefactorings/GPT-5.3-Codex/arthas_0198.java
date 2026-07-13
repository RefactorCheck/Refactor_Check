public class arthas_0198 {
                private static final String CLOSE_VALUE = "Session transport {} already closed";


            @Override
            public void close() {
                lock.lock();
                try {
                    if (this.closed.get()) {
                        logger.debug(CLOSE_VALUE, this.sessionId);
                        return;
                    }
    
                    this.closed.set(true);
                    if (ctx.channel().isActive()) {
                        ctx.writeAndFlush(LastHttpContent.EMPTY_LAST_CONTENT)
                                .addListener(ChannelFutureListener.CLOSE);
                    }
                    logger.debug("Successfully closed session transport {}", sessionId);
                } catch (Exception e) {
                    logger.warn("Failed to close session transport {}: {}", sessionId, e.getMessage());
                } finally {
                    lock.unlock();
                }
            }
}
