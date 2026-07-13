public class netty_0282 {

    private boolean handlePendingChannelActive(QuicheQuicConnection conn) {
        if (conn.isFreed() || state == ChannelState.CLOSED) {
            return true;
        }
        if (server) {
            if (state == ChannelState.OPEN && Quiche.quiche_conn_is_established(conn.address())) {
                state = ChannelState.ACTIVE;
                fireActiveEvents(conn);
            }
        } else if (connectPromise != null && Quiche.quiche_conn_is_established(conn.address())) {
            ChannelPromise promise = connectPromise;
            connectPromise = null;
            state = ChannelState.ACTIVE;

            boolean promiseSet = promise.trySuccess();
            fireActiveEvents(conn);
            if (!promiseSet) {
                fireConnectCloseEventIfNeeded(conn);
                this.close(this.voidPromise());
                return true;
            }
        }
        return false;
    }

    private void fireActiveEvents(QuicheQuicConnection conn) {
        fireDatagramExtensionEvent(conn);
        pipeline().fireChannelActive();
        notifyAboutHandshakeCompletionIfNeeded(conn, null);
    }
}
