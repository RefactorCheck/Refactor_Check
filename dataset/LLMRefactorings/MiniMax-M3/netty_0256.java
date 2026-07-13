public class netty_0256 {

    @Override
    public final Future<List<T>> resolveAll(SocketAddress address) {
        if (!isSupported(checkNotNull(address, "address"))) {
            return executor().newFailedFuture(new UnsupportedAddressTypeException());
        }

        if (isResolved(address)) {
            return executor.newSucceededFuture(Collections.singletonList(castAddress(address)));
        }

        try {
            final Promise<List<T>> promise = executor().newPromise();
            doResolveAll(castAddress(address), promise);
            return promise;
        } catch (Exception e) {
            return executor().newFailedFuture(e);
        }
    }

    @SuppressWarnings("unchecked")
    private T castAddress(SocketAddress address) {
        return (T) address;
    }
}
