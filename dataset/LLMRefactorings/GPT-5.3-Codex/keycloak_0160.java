private KeycloakTransaction getTransaction() {
            return extractGetTransaction();
                }
    
                @Override
                public void commit() {
                    runInvalidations();
                    transactionActive = false;
                }
    
                @Override
                public void rollback() {
                    setRollbackOnly = true;
                    runInvalidations();
                    transactionActive = false;
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
