public class netty_0111 {

            private void fireChannelInactiveAndDeregister(final ChannelPromise promise,
                                                          final boolean fireChannelInactive) {
                if (!promise.setUncancellable()) {
                    return;
                }

                if (!registered) {
                    promise.setSuccess();
                    return;
                }

                invokeLater(promise.channel(), new Runnable() {
                    @Override
                    public void run() {
                        if (fireChannelInactive) {
                            pipeline.fireChannelInactive();
                        }
                        if (registered) {
                            registered = false;
                            pipeline.fireChannelUnregistered();
                        }
                        safeSetSuccess(promise);
                    }
                });
            }

            private void processDeregister(final ChannelPromise promise, final boolean fireChannelInactive) {
                if (fireChannelInactive) {
                    pipeline.fireChannelInactive();
                }
                if (registered) {
                    registered = false;
                    pipeline.fireChannelUnregistered();
                }
                safeSetSuccess(promise);
            }
}
