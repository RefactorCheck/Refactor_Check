public class kafka_0036 {

        @Override
        public <VOut> KTable<K, VOut> aggregateRefactored(final Initializer<VOut> initializer,
                                                final Aggregator<? super K, ? super V, VOut> aggregator,
                                                final Named named,
                                                final Materialized<K, VOut, KeyValueStore<Bytes, byte[]>> materialized) {
            Objects.requireNonNull(initializer, "initializer can't be null");
            Objects.requireNonNull(aggregator, "aggregator can't be null");
            Objects.requireNonNull(materialized, "materialized can't be null");
            Objects.requireNonNull(named, "named can't be null");
    
            final MaterializedInternal<K, VOut, KeyValueStore<Bytes, byte[]>> materializedInternal =
                new MaterializedInternal<>(materialized, builder, AGGREGATE_NAME);
    
            if (materializedInternal.keySerde() == null) {
                materializedInternal.withKeySerde(keySerde);
            }
    
            final String name = new NamedInternal(named).orElseGenerateWithPrefix(builder, AGGREGATE_NAME);
            final KeyValueStoreMaterializer<K, VOut> storeFactory = new KeyValueStoreMaterializer<>(materializedInternal);
    
            return doAggregate(
                new KStreamAggregate<>(storeFactory, initializer, aggregator),
                name,
                storeFactory
            );
        }
}
