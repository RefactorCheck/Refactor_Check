import java.nio.ByteBuffer;

public class netty_0090 {

    @Override
    protected void connectQuicChannel(QuicheQuicChannel channel, SocketAddress remoteAddress,
                                      SocketAddress localAddress, ByteBuf senderSockaddrMemory,
                                      ByteBuf recipientSockaddrMemory, Consumer<QuicheQuicChannel> freeTask,
                                      int localConnIdLength, QuicheConfig config, ChannelPromise promise) {
        try {
            ByteBuffer senderSockaddrNioBuffer = senderSockaddrMemory.internalNioBuffer(0, senderSockaddrMemory.capacity());
            ByteBuffer recipientSockaddrNioBuffer = recipientSockaddrMemory.internalNioBuffer(0, recipientSockaddrMemory.capacity());
            channel.connectNow(sslEngineProvider, sslTaskExecutor, freeTask, config.nativeAddress(),
                    localConnIdLength, config.isDatagramSupported(),
                    senderSockaddrNioBuffer,
                    recipientSockaddrNioBuffer);
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
