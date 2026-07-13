public class netty_0196 {

    private void fulfillConnectPromise(ChannelPromise promise, boolean wasActive) {
        if (promise == null) {
            return;
        }

        boolean active = isActive();

        boolean promiseSet = promise.trySuccess();

        fireChannelActiveIfRequired(wasActive, active);

        if (!promiseSet) {
            close(voidPromise());
        }
    }

    private void fireChannelActiveIfRequired(boolean wasActive, boolean active) {
        if (!wasActive && active) {
            pipeline().fireChannelActive();
        }
    }
}
