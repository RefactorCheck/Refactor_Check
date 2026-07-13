public class netty_0291 {

        @Override
        public ChannelFuture goAway(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode,
                                    final ByteBuf debugData, ChannelPromise promise) {
            promise = promise.unvoid();
            final Http2Connection connection = connection();
            try {
                if (!connection.goAwaySent(lastStreamId, errorCode, debugData)) {
                    debugData.release();
                    promise.trySuccess();
                    return promise;
                }
            } catch (Throwable cause) {
                debugData.release();
                promise.tryFailure(cause);
                return promise;
            }

            debugData.retain();
            return writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);
        }

        private ChannelFuture writeGoAway(ChannelHandlerContext ctx, int lastStreamId, long errorCode,
                                          ByteBuf debugData, ChannelPromise promise) {
            ChannelFuture future = frameWriter().writeGoAway(ctx, lastStreamId, errorCode, debugData, promise);

            if (future.isDone()) {
                processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, future);
            } else {
                future.addListener((ChannelFutureListener) f ->
                        processGoAwayWriteResult(ctx, lastStreamId, errorCode, debugData, f));
            }

            return future;
        }
}
