public class netty_0256 {

        @Override
        public final Future<List<T>> resolveAllRefactored(SocketAddress address) {
            if (!isSupported(checkNotNull(address, "address"))) {
                // Address type not supported by the resolver
                return executor().newFailedFuture(new UnsupportedAddressTypeException());
            }
    
            if (isResolved(address)) {
                // Resolved already; no need to perform a lookup
                @SuppressWarnings("unchecked")
                final T cast = (T) address;
                return executor.newSucceededFuture(Collections.singletonList(cast));
            }
    
            try {
                @SuppressWarnings("unchecked")
                final T cast = (T) address;
                final Promise<List<T>> promise = executor().newPromise();
                doResolveAll(cast, promise);
                return promise;
            } catch (Exception e) {
                return executor().newFailedFuture(e);
            }
        }
}
