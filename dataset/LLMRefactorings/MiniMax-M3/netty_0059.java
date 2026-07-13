public class netty_0059 {

    private boolean cancelOps(boolean cancelConnect) {
        if (registration == null || !registration.isValid()) {
            return false;
        }
        boolean cancelled = false;
        byte flags = (byte) 0;
        if ((ioState & POLL_RDHUP_SCHEDULED) != 0 && pollRdhupId != 0) {
            submitPollCancel(pollRdhupId);
            pollRdhupId = 0;
            cancelled = true;
        }
        if ((ioState & POLL_IN_SCHEDULED) != 0 && pollInId != 0) {
            submitPollCancel(pollInId);
            pollInId = 0;
            cancelled = true;
        }
        if ((ioState & POLL_OUT_SCHEDULED) != 0 && pollOutId != 0) {
            submitPollCancel(pollOutId);
            pollOutId = 0;
            cancelled = true;
        }
        if (cancelConnect && connectId != 0) {
            // Best effort to cancel the already submitted connect request.
            long id = registration.submit(IoUringIoOps.newAsyncCancel(flags, connectId, Native.IORING_OP_CONNECT));
            assert id != 0;
            connectId = 0;
            cancelled = true;
        }
        if (numOutstandingReads != 0 || numOutstandingWrites != 0) {
            cancelled = true;
        }
        cancelOutstandingReads(registration, numOutstandingReads);
        cancelOutstandingWrites(registration, numOutstandingWrites);
        return cancelled;
    }

    private void submitPollCancel(long pollId) {
        long id = registration.submit(
                IoUringIoOps.newAsyncCancel((byte) 0, pollId, Native.IORING_OP_POLL_ADD));
        assert id != 0;
    }
}
