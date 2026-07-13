public class netty_0105 {

        private void writeHeadersFrameRefactored(final ChannelHandlerContext ctx, Http2HeadersFrame headersFrame,
                                       ChannelPromise promise) {
    
            if (isStreamIdValid(headersFrame.stream().id())) {
                encoder().writeHeaders(ctx, headersFrame.stream().id(), headersFrame.headers(), headersFrame.padding(),
                        headersFrame.isEndStream(), promise);
            } else if (initializeNewStream(ctx, (DefaultHttp2FrameStream) headersFrame.stream(), promise)) {
                promise = promise.unvoid();
    
                final int streamId = headersFrame.stream().id();
    
                encoder().writeHeaders(ctx, streamId, headersFrame.headers(), headersFrame.padding(),
                        headersFrame.isEndStream(), promise);
    
                if (!promise.isDone()) {
                    numBufferedStreams++;
                    // Clean up the stream being initialized if writing the headers fails and also
                    // decrement the number of buffered streams.
                    promise.addListener((ChannelFutureListener) channelFuture -> {
                        numBufferedStreams--;
                        handleHeaderFuture(channelFuture, streamId);
                    });
                } else {
                    handleHeaderFuture(promise, streamId);
                }
            }
        }
}
