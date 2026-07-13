public class netty_0275 {

        @Override
        protected void doBeginRead() throws Exception {
            if (readInProgress) {
                return;
            }
    
            Queue<Object> inboundBuffer = this.inboundBuffer;
            if (inboundBuffer.isEmpty()) {
                readInProgress = true;
                return;
            }
    
            final InternalThreadLocalMap threadLocals = InternalThreadLocalMap.get();
            final int stackDepth = threadLocals.localChannelReaderStackDepth();
            if (stackDepth < MAX_READER_STACK_DEPTH) {
                threadLocals.setLocalChannelReaderStackDepth(stackDepth + 1);
                try {
                    readInbound();
                } finally {
                    threadLocals.setLocalChannelReaderStackDepth(stackDepth);
                }
            } else {
                try {
                    eventLoop().execute(readTask);
                } catch (Throwable cause) {
                    logger.warn("Closing Local channels {}-{} because exception occurred!", this, peer, cause);
                    close();
                    peer.close();
                    PlatformDependent.throwException(cause);
                }
            }
        }
}
