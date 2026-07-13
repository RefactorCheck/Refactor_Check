public class netty_0287 {

    private void drainEventFd() {
        CompletionQueue completionQueue = ringBuffer.ioUringCompletionQueue();
        SubmissionQueue submissionQueue = ringBuffer.ioUringSubmissionQueue();
        assert !eventFdClosing;
        eventFdClosing = true;
        boolean eventPending = eventfdAsyncNotify.getAndSet(true);
        if (eventPending) {
            // There is an event that has been or will be written by another thread, so we must wait for the event.
            // Make sure we're actually listening for writes to the event fd.
            while (eventfdReadSubmitted == 0) {
                submitEventFdRead();
                submissionQueue.submit();
            }
            // Drain the eventfd of the pending wakup.
            class DrainFdEventCallback implements CompletionCallback {
                boolean eventFdDrained;

                @Override
                public boolean handle(int res, int flags, long udata, ByteBuffer extraCqeData) {
                    if (udata == EVENTFD_TOKEN) {
                        eventFdDrained = true;
                    }
                    return IoUringIoHandler.this.handle(res, flags, udata, extraCqeData);
                }
            }
            final DrainFdEventCallback handler = new DrainFdEventCallback();
            completionQueue.process(handler);
            while (!handler.eventFdDrained) {
                submissionQueue.submitAndGet();
                processCompletionsAndHandleOverflow(submissionQueue, completionQueue, handler);
            }
        }
        cancelPendingEventfdRead(submissionQueue);
    }

    private void cancelPendingEventfdRead(SubmissionQueue submissionQueue) {
        // We've consumed any pending eventfd read and `eventfdAsyncNotify` should never
        // transition back to false, thus we should never have any more events written.
        // So, if we have a read event pending, we can cancel it.
        if (eventfdReadSubmitted != 0) {
            submissionQueue.addCancel(eventfdReadSubmitted, EVENTFD_TOKEN);
            eventfdReadSubmitted = 0;
            submissionQueue.submit();
        }
    }
}
