public class netty_0214 {

        @Override
        public void flush(ChannelHandlerContext ctx) throws Exception {
            if (handleStartTls(ctx)) {
                return;
            }
    
            if (isStateSet(STATE_PROCESS_TASK)) {
                return;
            }
    
            try {
                wrapAndFlush(ctx);
            } catch (Throwable cause) {
                setHandshakeFailure(ctx, cause);
                PlatformDependent.throwException(cause);
            }
        }
    
        private boolean handleStartTls(ChannelHandlerContext ctx) throws Exception {
            if (startTls && !isStateSet(STATE_SENT_FIRST_MESSAGE)) {
                setState(STATE_SENT_FIRST_MESSAGE);
                pendingUnencryptedWrites.writeAndRemoveAll(ctx);
                forceFlush(ctx);
                startHandshakeProcessing(true);
                return true;
            }
            return false;
        }
}
