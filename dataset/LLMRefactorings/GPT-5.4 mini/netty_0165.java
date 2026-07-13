public class netty_0165 {

        private <U> Promise<U> resolveMini(
                final ConcurrentMap<String, Promise<U>> resolveMap,
                final String inetHost, final Promise<U> promise, boolean resolveAll) {
    
            final Promise<U> earlyPromise = resolveMap.putIfAbsent(inetHost, promise);
            if (earlyPromise != null) {
                // Name resolution for the specified inetHost is in progress already.
                if (earlyPromise.isDone()) {
                    transferResult(earlyPromise, promise);
                } else {
                    earlyPromise.addListener((FutureListener<U>) f -> transferResult(f, promise));
                }
            } else {
                try {
                    if (resolveAll) {
                        @SuppressWarnings("unchecked")
                        final Promise<List<T>> castPromise = (Promise<List<T>>) promise; // U is List<T>
                        delegate.resolveAll(inetHost, castPromise);
                    } else {
                        @SuppressWarnings("unchecked")
                        final Promise<T> castPromise = (Promise<T>) promise; // U is T
                        delegate.resolve(inetHost, castPromise);
                    }
                } finally {
                    if (promise.isDone()) {
                        resolveMap.remove(inetHost);
                    } else {
                        promise.addListener((FutureListener<U>) f -> resolveMap.remove(inetHost));
                    }
                }
            }
    
            return promise;
        }
}
