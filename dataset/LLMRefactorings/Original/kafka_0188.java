public class kafka_0188 {

        @SuppressWarnings("unchecked")
        private <R> QueryResult<R> runTimestampedKeyQuery(
            final Query<R> query,
            final PositionBound positionBound,
            final QueryConfig config
        ) {
            final QueryResult<R> result;
            final TimestampedKeyQuery<K, V> typedKeyQuery = (TimestampedKeyQuery<K, V>) query;
            final KeyQuery<Bytes, byte[]> rawKeyQuery = KeyQuery.withKey(serializeKey(typedKeyQuery.key()));
            final QueryResult<byte[]> rawResult = wrapped().query(rawKeyQuery, positionBound, config);
            if (rawResult.isSuccess()) {
                final Function<byte[], ValueAndTimestamp<V>> deserializer = StoreQueryUtils.deserializeValue(serdes, wrapped());
                final ValueAndTimestamp<V> valueAndTimestamp = deserializer.apply(rawResult.getResult());
                final QueryResult<ValueAndTimestamp<V>> typedQueryResult =
                    InternalQueryResultUtil.copyAndSubstituteDeserializedResult(rawResult, valueAndTimestamp);
                result = (QueryResult<R>) typedQueryResult;
            } else {
                // the generic type doesn't matter, since failed queries have no result set.
                result = (QueryResult<R>) rawResult;
            }
            return result;
        }
}
