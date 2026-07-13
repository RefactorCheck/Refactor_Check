public class kafka_0259 {

        @SuppressWarnings("unchecked")
        private <R> QueryResult<R> runTimestampedRangeQuery(
            final Query<R> query,
            final PositionBound positionBound,
            final QueryConfig config
        ) {
            final QueryResult<R> result;
            final TimestampedRangeQuery<K, V> typedQuery = (TimestampedRangeQuery<K, V>) query;
            RangeQuery<Bytes, byte[]> rawRangeQuery;
            final ResultOrder order = typedQuery.resultOrder();
            rawRangeQuery = RangeQuery.withRange(
                serializeKey(typedQuery.lowerBound().orElse(null)),
                serializeKey(typedQuery.upperBound().orElse(null))
            );
            if (order.equals(ResultOrder.DESCENDING)) {
                rawRangeQuery = rawRangeQuery.withDescendingKeys();
            }
            if (order.equals(ResultOrder.ASCENDING)) {
                rawRangeQuery = rawRangeQuery.withAscendingKeys();
            }
            final QueryResult<KeyValueIterator<Bytes, byte[]>> rawResult =
                wrapped().query(rawRangeQuery, positionBound, config);
            if (rawResult.isSuccess()) {
                final KeyValueIterator<Bytes, byte[]> iterator = rawResult.getResult();
                final KeyValueIterator<K, ValueAndTimestamp<V>> resultIterator =
                    (KeyValueIterator<K, ValueAndTimestamp<V>>) new MeteredTimestampedKeyValueStoreIterator(
                        iterator,
                        getSensor,
                        StoreQueryUtils.deserializeValue(serdes, wrapped()),
                        false
                    );
                final QueryResult<KeyValueIterator<K, ValueAndTimestamp<V>>> typedQueryResult =
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
