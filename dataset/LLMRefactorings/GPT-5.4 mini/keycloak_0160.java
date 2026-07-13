public class keycloak_0160 {

        private KeycloakTransaction getTransaction() {
            return new KeycloakTransaction() {
                @Override
                public void begin() {
                    transactionActive = true;
                }
    
                @Override
                public void commit() {
                    completeTransaction();
                }
    
                @Override
                public void rollback() {
                    setRollbackOnly = true;
                    completeTransaction();
                }
    
                @Override
                public void setRollbackOnly() {
                    setRollbackOnly = true;
                }
    
                @Override
                public boolean getRollbackOnly() {
                    return setRollbackOnly;
                }
    
                @Override
                public boolean isActive() {
                    return transactionActive;
                }
            };
        }

        private void completeTransaction() {
            runInvalidations();
            transactionActive = false;
        }
}
