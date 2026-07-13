public class netty_0291 {

        @Override
        public ChannelFuture goAwayRefactored(final ChannelHandlerContext ctx, final int lastStreamId, final long errorCode,
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
    
            // Need to retain before we write the buffer because if we do it after the refCnt could already be 0 and
            // result in an IllegalRefCountException.
            debugData.retain();
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
