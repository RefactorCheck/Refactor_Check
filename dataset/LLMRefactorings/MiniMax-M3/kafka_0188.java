public class kafka_0188 {

        @SuppressWarnings("unchecked")
        private <R> QueryResult<R> runTimestampedKeyQuery(
            final Query<R> query,
            final PositionBound positionBound,
            final QueryConfig config
        ) {
            final TimestampedKeyQuery<K, V> typedKeyQuery = (TimestampedKeyQuery<K, V>) query;
            final KeyQuery<Bytes, byte[]> rawKeyQuery = KeyQuery.withKey(serializeKey(typedKeyQuery.key()));
            final QueryResult<byte[]> rawResult = wrapped().query(rawKeyQuery, positionBound, config);
            if (rawResult.isSuccess()) {
                return (QueryResult<R>) deserializeTypedResult(rawResult);
            } else {
                return (QueryResult<R>) rawResult;
            }
        }

        private QueryResult<ValueAndTimestamp<V>> deserializeTypedResult(final QueryResult<byte[]> rawResult) {
            final Function<byte[], ValueAndTimestamp<V>> deserializer = StoreQueryUtils.deserializeValue(serdes, wrapped());
            final ValueAndTimestamp<V> valueAndTimestamp = deserializer.apply(rawResult.getResult());
            return InternalQueryResultUtil.copyAndSubstituteDeserializedResult(rawResult, valueAndTimestamp);
        }
}
