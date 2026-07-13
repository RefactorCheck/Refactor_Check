public class netty_0235 {
                private Http3HeadersFrame headersFrame;

        @Override
        void channelRead(ChannelHandlerContext ctx, Http3RequestStreamFrame frame) {
            if (frame instanceof Http3PushPromiseFrame) {
                if (server) {
                    // Server should not receive a push promise
                    // https://quicwg.org/base-drafts/draft-ietf-quic-http.html#name-push_promise
                    frameTypeUnexpected(ctx, frame);
                } else {
                    ctx.fireChannelRead(frame);
                }
                return;
            }
    
            if (frame instanceof Http3HeadersFrame) {
                this.headersFrame = (Http3HeadersFrame) frame;

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
}
