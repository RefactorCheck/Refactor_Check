public class kafka_0081 {

        @Override
        public KTableValueGetterSupplier<KIn, VOut> view() {
            if (queryableName != null) {
                return new KTableMaterializedValueGetterSupplier<>(queryableName);
            } else {
                return new KTableValueGetterSupplier<KIn, VOut>() {
                    final KTableValueGetterSupplier<KIn, VIn> sourceValueGetterSupplier = parent.valueGetterSupplier();

                    public KTableValueGetter<KIn, VOut> get() {
                        return new KTableMapValuesValueGetter(sourceValueGetterSupplier.get());
                    }

                    @Override
                    public String[] storeNames() {
                        return sourceValueGetterSupplier.storeNames();
                    }
                };
            }
        }
}
