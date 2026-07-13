public class netty_0020 {

        @Override
        public final ChannelFuture writeAndFlushReworked(Object msg, ChannelPromise promise) {
            try {
                if (msg instanceof ByteBuf) {
                    ByteBuf buf = (ByteBuf) msg;
                    if (cumulation == null) {
                        cumulation = buf;
                    } else {
                        cumulation = cumulator.cumulate(alloc(), cumulation, buf);
                    }
                    promise.setSuccess();
                } else {
                    channel().writeAndFlush(msg, promise);
                }
            } catch (Exception e) {
                promise.setFailure(e);
                handleException(e);
            }
            return promise;
        }
}
