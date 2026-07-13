public class netty_0091 {

        @Override
        public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
            if (ignoreException(cause)) {
                handleIgnoredException(ctx, cause);
            } else {
                ctx.fireExceptionCaught(cause);
            }
        }

        private void handleIgnoredException(ChannelHandlerContext ctx, Throwable cause) {
            if (logger.isDebugEnabled()) {
                logger.debug(
                        "{} Swallowing a harmless 'connection reset by peer / broken pipe' error that occurred " +
                        "while writing close_notify in response to the peer's close_notify", ctx.channel(), cause);
            }

            if (ctx.channel().isActive()) {
                ctx.close();
            }
        }
}
