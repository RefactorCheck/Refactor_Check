public class netty_0022 {

    @Override
    void epollInReady() {
        assert eventLoop().inEventLoop();
        final ChannelConfig config = config();
        if (shouldBreakEpollInReady(config)) {
            clearEpollIn0();
            return;
        }
        final EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
        final ChannelPipeline pipeline = pipeline();
        allocHandle.reset(config);
        allocHandle.attemptedBytesRead(1);

        try {
            Throwable exception = acceptChannels(allocHandle, pipeline);
            allocHandle.readComplete();
            pipeline.fireChannelReadComplete();

            if (exception != null) {
                pipeline.fireExceptionCaught(exception);
            }
        } finally {
            if (shouldStopReading(config)) {
                clearEpollIn();
            }
        }
    }

    private Throwable acceptChannels(EpollRecvByteAllocatorHandle allocHandle, ChannelPipeline pipeline) {
        try {
            do {
                allocHandle.lastBytesRead(socket.accept(acceptedAddress));
                if (allocHandle.lastBytesRead() == -1) {
                    break;
                }
                allocHandle.incMessagesRead(1);
                readPending = false;
                pipeline.fireChannelRead(newChildChannel(allocHandle.lastBytesRead(), acceptedAddress, 1,
                                                         acceptedAddress[0]));
            } while (allocHandle.continueReading());
            return null;
        } catch (Throwable t) {
            return t;
        }
    }
}
