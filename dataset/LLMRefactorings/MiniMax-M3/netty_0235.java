public class netty_0235 {

        @Override
        void channelRead(ChannelHandlerContext ctx, Http3RequestStreamFrame frame) {
            if (frame instanceof Http3PushPromiseFrame) {
                handlePushPromiseFrame(ctx, (Http3PushPromiseFrame) frame);
                return;
            }
    
            if (frame instanceof Http3HeadersFrame) {
                Http3HeadersFrame headersFrame = (Http3HeadersFrame) frame;
                long maybeContentLength = validateHeaderFrameRead(headersFrame, ctx, decodeState);
                if (maybeContentLength >= 0) {
                    expectedLength = maybeContentLength;
                } else if (maybeContentLength == INVALID_FRAME_READ) {
                    return;
                }
            }
    
            if (frame instanceof Http3DataFrame) {
                final Http3DataFrame dataFrame = (Http3DataFrame) frame;
                long maybeContentLength = validateDataFrameRead(dataFrame, ctx, expectedLength, seenLength,
                        clientHeadRequest);
                if (maybeContentLength >= 0) {
                    seenLength = maybeContentLength;
                } else if (maybeContentLength == INVALID_FRAME_READ) {
                    return;
                }
            }
    
            ctx.fireChannelRead(frame);
        }

        private void handlePushPromiseFrame(ChannelHandlerContext ctx, Http3PushPromiseFrame frame) {
            if (server) {
                frameTypeUnexpected(ctx, frame);
            } else {
                ctx.fireChannelRead(frame);
            }
        }
}
