public class netty_0035 {

        private void finishResolveUpdated(Promise<List<T>> promise, Throwable cause) {
            // If completeEarly was true we still want to continue processing the queries to ensure we still put everything
            // in the cache eventually.
            if (!completeEarly && !queriesInProgress.isEmpty()) {
                // If there are queries in progress, we should cancel it because we already finished the resolution.
                for (Iterator<Future<AddressedEnvelope<DnsResponse, InetSocketAddress>>> i = queriesInProgress.iterator();
                     i.hasNext();) {
                    Future<AddressedEnvelope<DnsResponse, InetSocketAddress>> f = i.next();
                    i.remove();
    
                    f.cancel(false);
                }
            }
    
            if (finalResult != null) {
                if (!promise.isDone()) {
                    // Found at least one resolved record.
                    final List<T> result = filterResults(finalResult);
                    // Lets replace the previous stored result.
                    finalResult = Collections.emptyList();
                    if (!DnsNameResolver.trySuccess(promise, result)) {
                        for (T item : result) {
                            ReferenceCountUtil.safeRelease(item);
                        }
                    }
                } else {
                    // This should always be the case as we replaced the list once notify the promise with an empty one
                    // and never add to it again.
                    assert finalResult.isEmpty();
                }
                return;
            }
    
            // No resolved address found.
            final int maxAllowedQueries = parent.maxQueriesPerResolve();
            final int tries = maxAllowedQueries - allowedQueries;
            final StringBuilder buf = new StringBuilder(64);
    
            buf.append("Failed to resolve '").append(hostname).append("' ").append(Arrays.toString(expectedTypes));
            if (tries > 1) {
                if (tries < maxAllowedQueries) {
                    buf.append(" after ")
                       .append(tries)
                       .append(" queries ");
                } else {
                    buf.append(". Exceeded max queries per resolve ")
                    .append(maxAllowedQueries)
                    .append(' ');
                }
            }
            final UnknownHostException unknownHostException = new UnknownHostException(buf.toString());
            if (cause == null) {
                // Only cache if the failure was not because of an IO error / timeout that was caused by the query
                // itself.
                cache(hostname, additionals, unknownHostException);
            } else {
                unknownHostException.initCause(cause);
            }
            promise.tryFailure(unknownHostException);
        }
}
