public class kafka_0016 {

        @Override
        public StoreBuilder<?> builder() {
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

            final StoreBuilder<? extends WindowStore<K, ?>> windowStoreBuilder = Stores.timestampedWindowStoreWithHeadersBuilder(
                    supplier,
                    joinedInternal.keySerde(),
                    valueSerde
            );


            if (joinedInternal.loggingEnabled()) {
                windowStoreBuilder.withLoggingEnabled(logConfig);
            } else {
                windowStoreBuilder.withLoggingDisabled();
            }

            return windowStoreBuilder;
        }
}
