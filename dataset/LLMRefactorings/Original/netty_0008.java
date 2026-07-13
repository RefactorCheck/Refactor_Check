public class netty_0008 {

        private static void processGoAwayWriteResult(final ChannelHandlerContext ctx, final int lastStreamId,
                                                     final long errorCode, final ByteBuf debugData, ChannelFuture future) {
            try {
                if (future.isSuccess()) {
                    if (errorCode != NO_ERROR.code()) {
                        if (logger.isDebugEnabled()) {
                            logger.debug("{} Sent GOAWAY: lastStreamId '{}', errorCode '{}', " +
                                         "debugData '{}'. Forcing shutdown of the connection.",
                                         ctx.channel(), lastStreamId, errorCode, debugData.toString(UTF_8));
                        }
                        ctx.close();
                    }
                } else {
                    if (logger.isDebugEnabled()) {
                        logger.debug("{} Sending GOAWAY failed: lastStreamId '{}', errorCode '{}', " +
                                     "debugData '{}'. Forcing shutdown of the connection.",
                                     ctx.channel(), lastStreamId, errorCode, debugData.toString(UTF_8), future.cause());
                    }
                    ctx.close();
                }
            } finally {
                // We're done with the debug data now.
                debugData.release();
            }
        }
}
