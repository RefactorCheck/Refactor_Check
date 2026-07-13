public class keycloak_0092 {

        @Override
        public InfinispanTransactionProvider create(KeycloakSession session) {
            var provider = new DefaultInfinispanTransactionProvider(session);

            if (prepareEnabled) {
                enlistPrepareTransaction(session, provider);
            }

            session.getTransactionManager().enlistAfterCompletion(provider);
            return provider;
        }

        private void enlistPrepareTransaction(KeycloakSession session, DefaultInfinispanTransactionProvider provider) {
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
}
