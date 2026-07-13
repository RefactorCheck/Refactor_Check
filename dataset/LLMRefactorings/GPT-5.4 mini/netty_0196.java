public class netty_0196 {

            private void fulfillConnectPromiseReworked(ChannelPromise promise, boolean wasActive) {
                if (promise == null) {
                    // Closed via cancellation and the promise has been notified already.
                    return;
                }
    
                // Get the state as trySuccess() may trigger an ChannelFutureListener that will close the Channel.
                // We still need to ensure we call fireChannelActive() in this case.
                boolean active = isActive();
    
                // trySuccess() will return false if a user cancelled the connection attempt.
                boolean promiseSet = promise.trySuccess();
    
                // Regardless if the connection attempt was cancelled, channelActive() event should be triggered,
                // because what happened is what happened.
                if (!wasActive && active) {
                    pipeline().fireChannelActive();
                }
    
                // If a user cancelled the connection attempt, close the channel, which is followed by channelInactive().
                if (!promiseSet) {
                    close(voidPromise());
                }
            }
}
