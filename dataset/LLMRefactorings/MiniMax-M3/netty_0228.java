public class netty_0228 {

    private void onHttp2GoAwayFrame(ChannelHandlerContext ctx, final Http2GoAwayFrame goAwayFrame) {
        if (goAwayFrame.lastStreamId() == Integer.MAX_VALUE) {
            // None of the streams can have an id greater than Integer.MAX_VALUE
            return;
        }
        // Notify which streams were not processed by the remote peer and are safe to retry on another connection:
        try {
            notifyUnprocessedStreams(goAwayFrame);
        } catch (Http2Exception e) {
            ctx.fireExceptionCaught(e);
            ctx.close();
        }
    }

    private void notifyUnprocessedStreams(final Http2GoAwayFrame goAwayFrame) throws Http2Exception {
        forEachActiveStream(new Http2FrameStreamVisitor() {
            @Override
            public boolean visit(Http2FrameStream stream) {
                final int streamId = stream.id();
                AbstractHttp2StreamChannel channel = (AbstractHttp2StreamChannel)
                        ((DefaultHttp2FrameStream) stream).attachment;
                if (streamId > goAwayFrame.lastStreamId() && connection().local().isValidStreamId(streamId)) {
                    channel.pipeline().fireUserEventTriggered(goAwayFrame.retainedDuplicate());
                }
                return true;
            }
        });
    }
}
