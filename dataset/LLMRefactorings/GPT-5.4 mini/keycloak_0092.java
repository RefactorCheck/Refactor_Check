public class keycloak_0092 {

        @Override
        public InfinispanTransactionProvider create(KeycloakSession session) {
            var provider = new DefaultInfinispanTransactionProvider(session);
            var transactionManager = session.getTransactionManager();
    
            if (prepareEnabled) {
                // In the prepare step, check if database entities can be locked. If this is true, no new transactions need to be started in the after-completion phase.
                transactionManager.enlistPrepare(new AbstractKeycloakTransaction() {
                    @Override
                    protected void commitImpl() {
                        provider.prepareStep();
                    }
    
                    @Override
                    protected void rollbackImpl() {
                    }
                });
            }
    
            transactionManager.enlistAfterCompletion(provider);
            return provider;
        }
}
