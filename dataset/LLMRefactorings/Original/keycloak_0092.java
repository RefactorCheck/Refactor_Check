public class keycloak_0092 {

        @Override
        public InfinispanTransactionProvider create(KeycloakSession session) {
            var provider = new DefaultInfinispanTransactionProvider(session);
    
            if (prepareEnabled) {
                // In the prepare step, check if database entities can be locked. If this is true, no new transactions need to be started in the after-completion phase.
                session.getTransactionManager().enlistPrepare(new AbstractKeycloakTransaction() {
                    @Override
                    protected void commitImpl() {
                        provider.prepareStep();
                    }
    
                    @Override
                    protected void rollbackImpl() {
                    }
                });
            }
    
            session.getTransactionManager().enlistAfterCompletion(provider);
            return provider;
        }
}
