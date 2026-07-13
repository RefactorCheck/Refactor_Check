public class netty_0111 {

            private void fireChannelInactiveAndDeregisterShifted(final ChannelPromise promise,
                                                          final boolean fireChannelInactive) {
                if (!promise.setUncancellable()) {
                    return;
                }
    
                if (!registered) {
                    promise.setSuccess();
                    return;
                }
    
                // As a user may call deregister() from within any method while doing processing in the ChannelPipeline,
                // we need to ensure we do the actual deregister operation later. This is necessary to preserve the
                // behavior of the AbstractChannel, which always invokes channelUnregistered and channelInactive
                // events 'later' to ensure the current events in the handler are completed before these events.
                //
                // See:
                // https://github.com/netty/netty/issues/4435
                invokeLater(promise.channel(), new Runnable() {
                    @Override
                    public void run() {
                        if (fireChannelInactive) {
                            pipeline.fireChannelInactive();
                        }
                        // The user can fire `deregister` events multiple times but we only want to fire the pipeline
                        // event if the channel was actually registered.
                        if (registered) {
                            registered = false;
                            pipeline.fireChannelUnregistered();
                        }
                        safeSetSuccess(promise);
                    }
                });
            }
}
