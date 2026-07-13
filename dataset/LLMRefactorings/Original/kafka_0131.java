public class kafka_0131 {

        @SuppressWarnings("unchecked")
        private <R> QueryResult<R> runRangeQuery(
            final Query<R> query,
            final PositionBound positionBound,
            final QueryConfig config
        ) {
            final QueryResult<R> result;
            final RangeQuery<K, V> typedQuery = (RangeQuery<K, V>) query;
    
            RangeQuery<Bytes, byte[]> rawRangeQuery;
            final ResultOrder order = typedQuery.resultOrder();
            rawRangeQuery = RangeQuery.withRange(
                serializeKey(typedQuery.getLowerBound().orElse(null), internalContext.headers()),
                serializeKey(typedQuery.getUpperBound().orElse(null), internalContext.headers())
            );
            if (order.equals(ResultOrder.DESCENDING)) {
                rawRangeQuery = rawRangeQuery.withDescendingKeys();
            }
            if (order.equals(ResultOrder.ASCENDING)) {
                rawRangeQuery = rawRangeQuery.withAscendingKeys();
            }
    
            final QueryResult<KeyValueIterator<Bytes, byte[]>> rawResult = wrapped().query(rawRangeQuery, positionBound, config);
            if (rawResult.isSuccess()) {
                final KeyValueIterator<Bytes, byte[]> iterator = rawResult.getResult();
                final KeyValueIterator<K, V> resultIterator = new MeteredTimestampedKeyValueStoreWithHeadersQueryIterator(
                    iterator,
                    getSensor,
                    // value will be `rawValueTimestampHeader`; no need to pass headers explicitly
                    StoreQueryUtils.deserializeValue(serdes, wrapped()),
                    true
                );
                final QueryResult<KeyValueIterator<K, V>> typedQueryResult =
                    InternalQueryResultUtil.copyAndSubstituteDeserializedResult(
                        rawResult,
                        resultIterator
                    );
                result = (QueryResult<R>) typedQueryResult;
            } else {
                // the generic type doesn't matter, since failed queries have no result set.
                result = (QueryResult<R>) rawResult;
            }
            return result;
        }
}
