public class kafka_0089 {

        private KTable<Windowed<K>, Long> doCount(final Named named,
                                                  final Materialized<K, Long, WindowStore<Bytes, byte[]>> materialized) {
            final MaterializedInternal<K, Long, WindowStore<Bytes, byte[]>> materializedInternal =
                    new MaterializedInternal<>(materialized, builder, AGGREGATE_NAME);
    
            if (materializedInternal.keySerde() == null) {
                materializedInternal.withKeySerde(keySerde);
            }
            if (materializedInternal.valueSerde() == null) {
                materializedInternal.withValueSerde(Serdes.Long());
            }
    
            final String aggregateName = new NamedInternal(named).orElseGenerateWithPrefix(builder, AGGREGATE_NAME);
            final StoreFactory storeFactory = new SlidingWindowStoreMaterializer<>(materializedInternal, windows, emitStrategy);
    
            return aggregateBuilder.buildWindowed(
                    new NamedInternal(aggregateName),
                    storeFactory.storeName(),
                    windows.gracePeriodMs(),
                    new KStreamSlidingWindowAggregate<>(windows, storeFactory, emitStrategy, aggregateBuilder.countInitializer, aggregateBuilder.countAggregator),
                    materializedInternal.queryableStoreName(),
                    materializedInternal.keySerde() != null ? new FullTimeWindowedSerde<>(materializedInternal.keySerde(), windows.timeDifferenceMs()) : null,
                    materializedInternal.valueSerde(),
                    false);
        }
}
