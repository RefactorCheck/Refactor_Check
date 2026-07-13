public class netty_0137 {

        @Override
        public int run(IoHandlerContext context) {
            if (closeCompleted) {
                if (context.shouldReportActiveIoTime()) {
                    context.reportActiveIoTime(0);
                }
                return 0;
            }
            SubmissionQueue submissionQueue = ringBuffer.ioUringSubmissionQueue();
            CompletionQueue completionQueue = ringBuffer.ioUringCompletionQueue();
            if (!completionQueue.hasCompletions() && context.canBlock()) {
                if (eventfdReadSubmitted == 0) {
                    submitEventFdRead();
                }
                long timeoutNanos = context.deadlineNanos() == -1 ? -1 : context.delayNanos(System.nanoTime());
                submitAndWaitWithTimeout(submissionQueue, false, timeoutNanos);
            } else {
                // Even if we have some completions already pending we can still try to even fetch more.
                submitAndClearNow(submissionQueue);
            }

            return processCompletionsAndReportIoTime(context, submissionQueue, completionQueue);
        }

        private int processCompletionsAndReportIoTime(IoHandlerContext context, SubmissionQueue submissionQueue, CompletionQueue completionQueue) {
            int ioCompletions;
            if (context.shouldReportActiveIoTime()) {
                long activeIoStartTimeNanos = System.nanoTime();
                ioCompletions = processCompletionsAndHandleOverflow(submissionQueue, completionQueue, this::handle);
                long activeIoEndTimeNanos = System.nanoTime();
                context.reportActiveIoTime(activeIoEndTimeNanos - activeIoStartTimeNanos);
            } else {
                ioCompletions = processCompletionsAndHandleOverflow(submissionQueue, completionQueue, this::handle);
            }
            return ioCompletions;
        }
}
