public class netty_0172 {

            private void register0(ChannelPromise promise) {
                // check if the channel is still open as it could be closed in the mean time when the register
                // call was outside of the eventLoop
                if (!promise.setUncancellable() || !ensureOpen(promise)) {
                    return;
                }
                ChannelPromise registerPromise = newPromise();
                boolean firstRegistration = neverRegistered;
                registerPromise.addListener(future -> {
                    if (future.isSuccess()) {
                        neverRegistered = false;
                        registered = true;
    
                        // Ensure we call handlerAdded(...) before we actually notify the promise. This is needed as the
                        // user may already fire events through the pipeline in the ChannelFutureListener.
                        pipeline.invokeHandlerAddedIfNeeded();
    
                        safeSetSuccess(promise);
                        pipeline.fireChannelRegistered();
                        // Only fire a channelActive if the channel has never been registered. This prevents firing
                        // multiple channel actives if the channel is deregistered and re-registered.
                        if (isActive()) {
                            if (firstRegistration) {
                                pipeline.fireChannelActive();
                            } else if (config().isAutoRead()) {
                                // This channel was registered before and autoRead() is set. This means we need to
                                // begin read again so that we process inbound data.
                                //
                                // See https://github.com/netty/netty/issues/4805
                                beginRead();
                            }
                        }
                    } else {
                        // Close the channel directly to avoid FD leak.
                        close(newPromise());
                        closeFuture.setClosed();
                        safeSetFailure(promise, future.cause());
                    }
                });
                doRegister(registerPromise);
            }
}
