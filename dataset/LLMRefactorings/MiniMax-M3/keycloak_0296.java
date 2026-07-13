public class keycloak_0296 {

        private PasswordHashProvider getPasswordHashProvider(KeycloakSession session, PasswordPolicy passwordPolicy) {
            if (passwordPolicy != null && passwordPolicy.getHashAlgorithm() != null) {
                return session.getProvider(PasswordHashProvider.class, passwordPolicy.getHashAlgorithm());
            }
            return session.getProvider(PasswordHashProvider.class);
        }

        private void rehashPasswordIfRequired(KeycloakSession session, RealmModel realm, UserModel user, CredentialInput input, PasswordCredentialModel password) {
            PasswordPolicy passwordPolicy = realm.getPasswordPolicy();
            PasswordHashProvider provider = getPasswordHashProvider(session, passwordPolicy);
    
            if (!provider.policyCheck(passwordPolicy, password)) {
                final int iterations = passwordPolicy != null ? passwordPolicy.getHashIterations() : -1;
                final String hashAlgorithm = passwordPolicy != null ? passwordPolicy.getHashAlgorithm() : null;
                // Refresh the password in a different transaction, do not fail if there is a model exception on current modifications due to concurrent logins.
                // Also do not start it as a nested transaction, as the current transaction might have auto-migrated the credential.
                // see: JpaUserCredentialStore#toModel for the on-the-fly migration of the salt column
                session.getTransactionManager().enlistAfterCompletion(new AbstractKeycloakTransaction() {
                    @Override
                    protected void commitImpl() {
                        try {
                            KeycloakModelUtils.runJobInTransaction(session.getKeycloakSessionFactory(), session.getContext(),
                                    (KeycloakSession s) -> refreshPassword(s, hashAlgorithm, iterations, input.getChallengeResponse(),
                                            password.getId(), password.getCreatedDate(), password.getUserLabel(), user.getId()));
                        } catch (ModelException e) {
                            logger.info("Error re-hashing the password in a different transaction", e);
                        }
                    }
    
                    @Override
                    protected void rollbackImpl() {
    
                    }
                });
            }
        }
}
