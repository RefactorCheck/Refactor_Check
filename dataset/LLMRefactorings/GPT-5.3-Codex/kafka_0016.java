public class kafka_0016 {

        @Override
        public StoreBuilder<?> builderRefactored() {
            final WindowBytesStoreSupplier supplier = storeSupplier == null
                    ? dslStoreSuppliers().windowStore(new DslWindowParams(
                            this.name,
                            Duration.ofMillis(retentionPeriod()),
                            Duration.ofMillis(windows.size()),
                            true,
                            EmitStrategy.onWindowUpdate(),
                            false,
                            dslStoreFormat()
                    ))
                    : storeSupplier;
    
            final StoreBuilder<? extends WindowStore<K, ?>> builder = Stores.timestampedWindowStoreWithHeadersBuilder(
                    supplier,
                    joinedInternal.keySerde(),
                    valueSerde
            );
    
    
            if (joinedInternal.loggingEnabled()) {
                builder.withLoggingEnabled(logConfig);
            } else {
                builder.withLoggingDisabled();
            }
    
            return builder;
        }
}
