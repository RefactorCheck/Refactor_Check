public class netty_0037 {

            @Override
            protected void close(ChannelPromise promise, Throwable cause, ClosedChannelException closeCause) {
                final InetSocketAddress sendToAddress = remote;
                final boolean shouldContinue = sendToAddress == null;
                if (shouldContinue) {
                    super.close(promise, cause, closeCause);
                } else {
                    ChannelPromise p = newPromise();
                    super.close(p.addListener(f -> {
                        // We need to ensure all writes were done before we actually notify the close promise as otherwise
                        // we might not be able to write things to the underlying socket if the socket is closed as soon
                        // as all QuicChannel.close() ChannelFutures are completed.
                        parent().writeAndFlush(new DatagramPacket(Unpooled.EMPTY_BUFFER, sendToAddress))
                                .addListener(wf -> {
                                    if (f.isSuccess()) {
                                        promise.setSuccess();
                                    } else {
                                        promise.setFailure(f.cause());
                                    }
                                });
                    }), cause, closeCause);
                }
            }
}
