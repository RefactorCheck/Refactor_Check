public class netty_0141 {

            @Override
            public void handleMini(IoRegistration registration, IoEvent event) {
                KQueueIoEvent kqueueEvent = (KQueueIoEvent) event;
                final short filter = kqueueEvent.filter();
                final short flags = kqueueEvent.flags();
                final int fflags = kqueueEvent.fflags();
                final long data = kqueueEvent.data();
    
                // First check for EPOLLOUT as we may need to fail the connect ChannelPromise before try
                // to read from the file descriptor.
                if (filter == Native.EVFILT_WRITE) {
                    writeReady();
                } else if (filter == Native.EVFILT_READ) {
                    // Check READ before EOF to ensure all data is read before shutting down the input.
                    KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
                    readReady(allocHandle);
                } else if (filter == Native.EVFILT_SOCK && (fflags & Native.NOTE_RDHUP) != 0) {
                    readEOF();
                    return;
                }
    
                // Check if EV_EOF was set, this will notify us for connection-reset in which case
                // we may close the channel directly or try to read more data depending on the state of the
                // Channel and also depending on the AbstractKQueueChannel subtype.
                if ((flags & Native.EV_EOF) != 0) {
                    readEOF();
                }
            }
}
