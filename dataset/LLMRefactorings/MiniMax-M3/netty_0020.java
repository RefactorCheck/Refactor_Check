public class netty_0020 {

        @Override
        public final ChannelFuture writeAndFlush(Object msg, ChannelPromise promise) {
            try {
                if (msg instanceof ByteBuf) {
                    cumulate((ByteBuf) msg);
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

        private void cumulate(ByteBuf buf) {
            if (cumulation == null) {
                cumulation = buf;
            } else {
                cumulation = cumulator.cumulate(alloc(), cumulation, buf);
            }
        }
}
