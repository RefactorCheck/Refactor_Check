public class netty_0090 {

        @Override
        protected void connectQuicChannelAdjusted(QuicheQuicChannel channel, SocketAddress remoteAddress,
                                          SocketAddress localAddress, ByteBuf senderSockaddrMemory,
                                          ByteBuf recipientSockaddrMemory, Consumer<QuicheQuicChannel> freeTask,
                                          int localConnIdLength, QuicheConfig config, ChannelPromise promise) {
            try {
                channel.connectNow(sslEngineProvider, sslTaskExecutor, freeTask, config.nativeAddress(),
                        localConnIdLength, config.isDatagramSupported(),
                        senderSockaddrMemory.internalNioBuffer(0, senderSockaddrMemory.capacity()),
                        recipientSockaddrMemory.internalNioBuffer(0, recipientSockaddrMemory.capacity()));
            } catch (Throwable cause) {
                // Only fail the original promise. Cleanup will be done as part of the listener attached to it.
                promise.setFailure(cause);
                return;
            }
    
            addChannel(channel);
            channel.finishConnect();
            promise.setSuccess();
        }
}
