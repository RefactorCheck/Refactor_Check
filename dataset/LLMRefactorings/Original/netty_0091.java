public class netty_0091 {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (ignoreException(cause)) {
                // It is safe to ignore the 'connection reset by peer' or
                // 'broken pipe' error after sending close_notify.
                if (logger.isDebugEnabled()) {
                    logger.debug(
                            "{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred " +
                            "while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
                }
    
                // Close the connection explicitly just in case the transport
                // did not close the connection automatically.
                if (ctx.channel().isActive()) {
                    ctx.close();
                }
            } else {
                ctx.fireExceptionCaught(cause);
            }
        }
}
