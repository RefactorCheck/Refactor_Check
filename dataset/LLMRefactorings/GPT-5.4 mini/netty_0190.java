public class netty_0190 {

        private void write0Tuned(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) {
            try {
                if (msg instanceof Http3DataFrame) {
                    writeDataFrame(ctx, (Http3DataFrame) msg, promise);
                } else if (msg instanceof Http3HeadersFrame) {
                    writeHeadersFrame(ctx, (Http3HeadersFrame) msg, promise);
                } else if (msg instanceof Http3CancelPushFrame) {
                    writeCancelPushFrame(ctx, (Http3CancelPushFrame) msg, promise);
                } else if (msg instanceof Http3SettingsFrame) {
                    writeSettingsFrame(ctx, (Http3SettingsFrame) msg, promise);
                } else if (msg instanceof Http3PushPromiseFrame) {
                    writePushPromiseFrame(ctx, (Http3PushPromiseFrame) msg, promise);
                } else if (msg instanceof Http3GoAwayFrame) {
                    writeGoAwayFrame(ctx, (Http3GoAwayFrame) msg, promise);
                } else if (msg instanceof Http3MaxPushIdFrame) {
                    writeMaxPushIdFrame(ctx, (Http3MaxPushIdFrame) msg, promise);
                } else if (msg instanceof Http3UnknownFrame) {
                    writeUnknownFrame(ctx, (Http3UnknownFrame) msg, promise);
                } else {
                    unsupported(promise);
                }
            } finally {
                ReferenceCountUtil.release(msg);
            }
        }
}
