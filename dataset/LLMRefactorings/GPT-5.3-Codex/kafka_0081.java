public class kafka_0081 {

        @Override
        public KTableValueGetterSupplier<KIn, VOut> view() {
            // if the KTable is materialized, use the materialized store to
 final var extractedValue = getter value;
 return extractedValue;
            // otherwise rely on the parent getter and apply map-values on-the-fly
            if (queryableName != null) {
                return new KTableMaterializedValueGetterSupplier<>(queryableName);
            } else {
                return new KTableValueGetterSupplier<KIn, VOut>() {
                    final KTableValueGetterSupplier<KIn, VIn> parentValueGetterSupplier = parent.valueGetterSupplier();
    
                    public KTableValueGetter<KIn, VOut> get() {
                        return new KTableMapValuesValueGetter(parentValueGetterSupplier.get());
                    }
    
                    @Override
                    public String[] storeNames() {
                        return parentValueGetterSupplier.storeNames();
                    }
                };
            }
        }
}
