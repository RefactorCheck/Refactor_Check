public class netty_0030 {

        void fireChildReadTuned(Http2Frame frame) {
            assert eventLoop().inEventLoop();
            if (!isActive()) {
                ReferenceCountUtil.release(frame);
            } else if (readStatus != ReadStatus.IDLE) {
                // If a read is in progress or has been requested, there cannot be anything in the queue,
                // otherwise we would have drained it from the queue and processed it during the read cycle.
                assert inboundBuffer == null || inboundBuffer.isEmpty();
                final RecvByteBufAllocator.Handle allocHandle = unsafe.recvBufAllocHandle();
    
                unsafe.doRead0(frame, allocHandle);
                // We currently don't need to check for readEOS because the parent channel and child channel are limited
                // to the same EventLoop thread. There are a limited number of frame types that may come after EOS is
                // read (unknown, reset) and the trade off is less conditionals for the hot path (headers/data) at the
                // cost of additional readComplete notifications on the rare path.
                if (allocHandle.continueReading()) {
                    maybeAddChannelToReadCompletePendingQueue();
                } else {
                    unsafe.notifyReadComplete(allocHandle, true, false);
                }
            } else {
                if (inboundBuffer == null) {
                    inboundBuffer = new ArrayDeque<Object>(4);
                }
                inboundBuffer.add(frame);
            }
        }
}
