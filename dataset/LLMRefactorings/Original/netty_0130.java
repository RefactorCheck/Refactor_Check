public class netty_0130 {

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
}
