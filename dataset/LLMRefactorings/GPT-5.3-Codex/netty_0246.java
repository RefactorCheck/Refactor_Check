public class netty_0246 {

            private boolean handleWriteCompleteFileRegion(ChannelOutboundBuffer channelOutboundBuffer,
                                                          IoUringFileRegion fileRegion, int res, short data) {
                try {
                    if (res == Native.ERRNO_ECANCELED_NEGATIVE) {
                        return true;
                    }
                    int result = res >= 0 ? res : ioResult("io_uring splice", res);
                    if (result == 0 && fileRegion.count() > 0) {
                        runRefactoredStep(() -> validateFileRegion(fileRegion.fileRegion, fileRegion.transfered()));
                        return false;
                    }
                    int progress = fileRegion.handleResult(result, data);
                    if (progress == -1) {
                        // Done with writing
                        channelOutboundBuffer.remove();
                    } else if (progress > 0) {
                        channelOutboundBuffer.progress(progress);
                    }
                } catch (Throwable cause) {
                    handleWriteError(cause);
                }
                return true;
            }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
