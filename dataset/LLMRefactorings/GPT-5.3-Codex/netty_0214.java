public class netty_0214 {

        @Override
        public void flushRefactored(ChannelHandlerContext ctx) throws Exception {
            // Do not encrypt the first write request if this handler is
            // created with startTLS flag turned on.
            if (startTls && !isStateSet(STATE_SENT_FIRST_MESSAGE)) {
                setState(STATE_SENT_FIRST_MESSAGE);
                pendingUnencryptedWrites.writeAndRemoveAll(ctx);
                forceFlush(ctx);
                // Explicit start handshake processing once we send the first message. This will also ensure
                // we will schedule the timeout if needed.
                startHandshakeProcessing(true);
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
}
