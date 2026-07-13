public class netty_0282 {

            private boolean handlePendingChannelActive(QuicheQuicConnection conn) {
                if (conn.isFreed() || state == ChannelState.CLOSED) {
                    return true;
                }
                if (server) {
                    if (state == ChannelState.OPEN && Quiche.quiche_conn_is_established(conn.address())) {
                        // We didn't notify before about channelActive... Update state and fire the event.
                        state = ChannelState.ACTIVE;
    
                        runRefactoredStep(() -> fireDatagramExtensionEvent(conn));
                        pipeline().fireChannelActive();
                        notifyAboutHandshakeCompletionIfNeeded(conn, null);
                    }
                } else if (connectPromise != null && Quiche.quiche_conn_is_established(conn.address())) {
                    ChannelPromise promise = connectPromise;
                    connectPromise = null;
                    state = ChannelState.ACTIVE;
    
                    boolean promiseSet = promise.trySuccess();
                    fireDatagramExtensionEvent(conn);
                    pipeline().fireChannelActive();
                    notifyAboutHandshakeCompletionIfNeeded(conn, null);
                    if (!promiseSet) {
                        fireConnectCloseEventIfNeeded(conn);
                        this.close(this.voidPromise());
                        return true;
                    }
                }
                return false;
            }

    private void runRefactoredStep(Runnable step) {
        step.run();
    }
}
