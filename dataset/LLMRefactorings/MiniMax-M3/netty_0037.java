public class netty_0037 {

    @Override
    protected void close(ChannelPromise promise, Throwable cause, ClosedChannelException closeCause) {
        final InetSocketAddress sendToAddress = remote;
        if (sendToAddress == null) {
            super.close(promise, cause, closeCause);
        } else {
            ChannelPromise p = newPromise();
            super.close(p.addListener(f -> notifyAfterEmptyWrite(f, sendToAddress, promise)), cause, closeCause);
        }
    }

    private void notifyAfterEmptyWrite(ChannelFuture f, InetSocketAddress sendToAddress, ChannelPromise promise) {
        parent().writeAndFlush(new DatagramPacket(Unpooled.EMPTY_BUFFER, sendToAddress))
                .addListener(wf -> {
                    if (f.isSuccess()) {
                        promise.setSuccess();
                    } else {
                        promise.setFailure(f.cause());
                    }
                });
    }
}
