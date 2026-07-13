public class netty_0050 {

        void resolve(final Promise<List<T>> promise) {
            final String[] searchDomains = parent.searchDomains();
            if (searchDomains.length == 0 || parent.ndots() == 0 || StringUtil.endsWith(hostname, '.')) {
                internalResolve(hostname, promise);
            } else {
                final boolean startWithoutSearchDomain = hasNDots();
                final String initialHostname = startWithoutSearchDomain ? hostname : hostname + '.' + searchDomains[0];
                final int initialSearchDomainIdx = startWithoutSearchDomain ? 0 : 1;
    
                final Promise<List<T>> searchDomainPromise = parent.executor().newPromise();
                searchDomainPromise.addListener(new FutureListener<List<T>>() {
                    private int searchDomainIdx = initialSearchDomainIdx;
                    @Override
                    public void operationComplete(Future<List<T>> future) {
                        Throwable cause = future.cause();
                        if (cause == null) {
                            final List<T> result = future.getNow();
                            if (!promise.trySuccess(result)) {
                                for (T item : result) {
                                    ReferenceCountUtil.safeRelease(item);
                                }
                            }
                        } else {
                            if (DnsNameResolver.isTransportOrTimeoutError(cause)) {
                                promise.tryFailure(new SearchDomainUnknownHostException(cause, hostname, expectedTypes,
                                        searchDomains));
                            } else if (searchDomainIdx < searchDomains.length) {
                                Promise<List<T>> newPromise = parent.executor().newPromise();
                                newPromise.addListener(this);
                                doSearchDomainQuery(hostname + '.' + searchDomains[searchDomainIdx++], newPromise);
                            } else if (!startWithoutSearchDomain) {
                                internalResolve(hostname, promise);
                            } else {
                                promise.tryFailure(new SearchDomainUnknownHostException(cause, hostname, expectedTypes,
                                        searchDomains));
                            }
                        }
                    }
                });
                doSearchDomainQuery(initialHostname, searchDomainPromise);
            }
        }
}
