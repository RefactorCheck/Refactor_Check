public class netty_0022 {

            @Override
            void epollInReady() {
                final int EXTRACTED_VALUE = 1;
                assert eventLoop().inEventLoop();
                final ChannelConfig config = config();
                if (shouldBreakEpollInReady(config)) {
                    clearEpollIn0();
                    return;
                }
                final EpollRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
                final ChannelPipeline pipeline = pipeline();
                allocHandle.reset(config);
                allocHandle.attemptedBytesRead(EXTRACTED_VALUE);
    
                Throwable exception = null;
                try {
                    try {
                        do {
                            // lastBytesRead represents the fd. We use lastBytesRead because it must be set so that the
                            // EpollRecvByteAllocatorHandle knows if it should try to read again or not when autoRead is
                            // enabled.
                            allocHandle.lastBytesRead(socket.accept(acceptedAddress));
                            if (allocHandle.lastBytesRead() == -EXTRACTED_VALUE) {
                                // this means everything was handled for now
                                break;
                            }
                            allocHandle.incMessagesRead(EXTRACTED_VALUE);
    
                            readPending = false;
                            pipeline.fireChannelRead(newChildChannel(allocHandle.lastBytesRead(), acceptedAddress, EXTRACTED_VALUE,
                                                                     acceptedAddress[0]));
                        } while (allocHandle.continueReading());
                    } catch (Throwable t) {
                        exception = t;
                    }
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
}
