public class kafka_0105 {

        public ShareFetch<K, V> collect(final ShareFetchBuffer fetchBuffer) {
            ShareFetch<K, V> fetch = ShareFetch.empty();
            int recordsRemaining = shareFetchConfig.maxPollRecords;
    
            try {
                while (recordsRemaining > 0) {
                    final ShareCompletedFetch nextInLineFetch = fetchBuffer.nextInLineFetch();
    
                    if (nextInLineFetch == null || nextInLineFetch.isConsumed()) {
                        final ShareCompletedFetch completedFetch = fetchBuffer.peek();
    
                        if (completedFetch == null) {
                            break;
                        }
    
                        if (!completedFetch.isInitialized()) {
                            try {
                                fetchBuffer.setNextInLineFetch(initialize(completedFetch));
                            } catch (Exception e) {
                                if (fetch.isEmpty()) {
                                    fetchBuffer.poll();
                                }
                                throw e;
                            }
                        } else {
                            fetchBuffer.setNextInLineFetch(completedFetch);
                        }
    
                        fetchBuffer.poll();
                    } else {
                        final TopicIdPartition tp = nextInLineFetch.partition;
    
                        ShareInFlightBatch<K, V> batch = nextInLineFetch.fetchRecords(
                                deserializers,
                                recordsRemaining,
                                shareFetchConfig.checkCrcs);
    
                        if (batch.isEmpty()) {
                            nextInLineFetch.drain();
                        }
    
                        recordsRemaining -= batch.numRecords();
                        fetch.add(tp, batch);
    
                        if (batch.getException() != null) {
                            throw new ShareFetchException(fetch, batch.getException().cause());
                        } else if (batch.hasCachedException()) {
                            break;
                        }
                    }
                }
            } catch (KafkaException e) {
                if (fetch.isEmpty()) {
                    throw e;
                }
            }
    
            return fetch;
        }
}
