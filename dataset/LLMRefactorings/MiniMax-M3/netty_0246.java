public class netty_0246 {

    private boolean handleWriteCompleteFileRegion(ChannelOutboundBuffer channelOutboundBuffer,
                                                  IoUringFileRegion fileRegion, int res, short data) {
        try {
            if (res == Native.ERRNO_ECANCELED_NEGATIVE) {
                return true;
            }
            int result = res >= 0 ? res : ioResult("io_uring splice", res);
            if (result == 0 && fileRegion.count() > 0) {
                validateFileRegion(fileRegion.fileRegion, fileRegion.transfered());
                return false;
            }
            updateBufferProgress(channelOutboundBuffer, fileRegion, result, data);
        } catch (Throwable cause) {
            handleWriteError(cause);
        }
        return true;
    }

    private void updateBufferProgress(ChannelOutboundBuffer channelOutboundBuffer,
                                      IoUringFileRegion fileRegion, int result, short data) {
        int progress = fileRegion.handleResult(result, data);
        if (progress == -1) {
            channelOutboundBuffer.remove();
        } else if (progress > 0) {
            channelOutboundBuffer.progress(progress);
        }
    }
}
