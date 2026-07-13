public class kafka_0092 {

        @Override
        public <VOut> KTable<Windowed<K>, VOut> aggregate(final Initializer<VOut> initializer,
                                                          final Aggregator<? super K, ? super V, VOut> aggregator,
                                                          final Named named,
                                                          final Materialized<K, VOut, WindowStore<Bytes, byte[]>> materialized) {
            Objects.requireNonNull(initializer, "initializer can't be null");
            Objects.requireNonNull(aggregator, "aggregator can't be null");
            Objects.requireNonNull(materialized, "materialized can't be null");
            final MaterializedInternal<K, VOut, WindowStore<Bytes, byte[]>> materializedInternal =
                new MaterializedInternal<>(materialized, builder, AGGREGATE_NAME);
            if (materializedInternal.keySerde() == null) {
                materializedInternal.withKeySerde(keySerde);
            }
            final String aggregateName = new NamedInternal(named).orElseGenerateWithPrefix(builder, AGGREGATE_NAME);
            final StoreFactory storeFactory = new SlidingWindowStoreMaterializer<>(materializedInternal, windows, emitStrategy);
    
            return aggregateBuilder.buildWindowed(
                new NamedInternal(aggregateName),
                storeFactory.storeName(),
                windows.gracePeriodMs(),
                new KStreamSlidingWindowAggregate<>(windows, storeFactory, emitStrategy, initializer, aggregator),
                materializedInternal.queryableStoreName(),
                materializedInternal.keySerde() != null ? new FullTimeWindowedSerde<>(materializedInternal.keySerde(), windows.timeDifferenceMs()) : null,
                materializedInternal.valueSerde(),
                false);
        }
}
