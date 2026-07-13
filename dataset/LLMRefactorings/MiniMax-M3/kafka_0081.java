public class kafka_0081 {

        @Override
        public KTableValueGetterSupplier<KIn, VOut> view() {
            if (queryableName != null) {
                return new KTableMaterializedValueGetterSupplier<>(queryableName);
            } else {
                return createParentValueGetterSupplier();
            }
        }

        private KTableValueGetterSupplier<KIn, VOut> createParentValueGetterSupplier() {
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
