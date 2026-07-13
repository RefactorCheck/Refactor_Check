public class netty_0056 {

            @Override
            public void connect(final SocketAddress remoteAddress,
                    SocketAddress localAddress, final ChannelPromise promise) {
                if (!promise.setUncancellable() || !ensureOpen(promise)) {
                    return;
                }
    
                if (state == State.CONNECTED) {
                    Exception cause = new AlreadyConnectedException();
                    safeSetFailure(promise, cause);
                    return;
                }
    
                if (connectPromise != null) {
                    throw new ConnectionPendingException();
                }
    
                connectPromise = promise;
    
                if (state != State.BOUND) {
                    // Not bound yet and no localAddress specified - get one.
                    if (localAddress == null) {
                        localAddress = new LocalAddress(LocalChannel.this);
                    }
                }
    
                if (localAddress != null) {
                    try {
                        doBind(localAddress);
                    } catch (Throwable t) {
                        safeSetFailure(promise, t);
                        close(voidPromise());
                        return;
                    }
                }
    
                Channel boundChannel = LocalChannelRegistry.get(remoteAddress);
                if (!(boundChannel instanceof LocalServerChannel)) {
                    Exception cause = new ConnectException("connection refused: " + remoteAddress);
                    safeSetFailure(promise, cause);
                    close(voidPromise());
                    return;
                }
    
                LocalServerChannel serverChannel = (LocalServerChannel) boundChannel;
                peer = serverChannel.serve(LocalChannel.this);
            }
}
