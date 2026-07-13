public class kafka_0009 {

        @SuppressWarnings("unchecked")
        @Override
        public <S extends StateStore> S  getStateStore(final String name) {
            throwUnsupportedOperationExceptionIfStandby("getStateStore");
            if (currentNode() == null) {
                throw new StreamsException("Accessing from an unknown node");
            }
    
            final StateStore globalStore = stateManager.globalStore(name);
            if (globalStore != null) {
                return (S) getReadOnlyStore(globalStore);
            }
    
            validateStoreAccess(name);
    
            final StateStore store = stateManager.store(name);
            return (S) wrapWithReadWriteStore(store);
        }

        private void validateStoreAccess(final String name) {
            if (!currentNode().stateStores.contains(name)) {
                throw new StreamsException("Processor " + currentNode().name() + " has no access to StateStore " + name +
                    " as the store is not connected to the processor. If you add stores manually via '.addStateStore()' " +
                    "make sure to connect the added store to the processor by providing the processor name to " +
                    "'.addStateStore()' or connect them via '.connectProcessorAndStateStores()'. " +
                    "DSL users need to provide the store name to '.process()', '.processValues()', or '.transformValues()' " +
                    "to connect the store to the corresponding operator, or they can provide a StoreBuilder by implementing " +
                    "the stores() method on the Supplier itself. If you do not add stores manually, " +
                    "please file a bug report at https://issues.apache.org/jira/projects/KAFKA.");
            }
        }
}
