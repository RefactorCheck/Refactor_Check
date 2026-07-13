public class netty_0141 {

    @Override
    public void handle(IoRegistration registration, IoEvent event) {
        KQueueIoEvent kqueueEvent = (KQueueIoEvent) event;
        final short filter = kqueueEvent.filter();
        final short flags = kqueueEvent.flags();
        final int fflags = kqueueEvent.fflags();
        final long data = kqueueEvent.data();

        if (filter == Native.EVFILT_WRITE) {
            writeReady();
        } else if (filter == Native.EVFILT_READ) {
            KQueueRecvByteAllocatorHandle allocHandle = recvBufAllocHandle();
            readReady(allocHandle);
        } else if (filter == Native.EVFILT_SOCK && (fflags & Native.NOTE_RDHUP) != 0) {
            readEOF();
            return;
        }

        handleEof(flags);
    }

    private void handleEof(short flags) {
        if ((flags & Native.EV_EOF) != 0) {
            readEOF();
        }
    }
}
