public class keycloak_0213 {

        private void updateGrantedConsentEntity(UserConsentEntity consentEntity, UserConsentModel consentModel) {
            Collection<UserConsentClientScopeEntity> grantedClientScopeEntities = consentEntity.getGrantedClientScopes();
            Collection<UserConsentClientScopeEntity> scopesToRemove = new HashSet<>(grantedClientScopeEntities);

            for (ClientScopeModel clientScope : consentModel.getGrantedClientScopes()) {
                if (ClientScopeModel.isParameterizedScope(clientScope)) {
                    consentModel.getParameters(clientScope).forEach(p -> createUserConsentClientScopeEntity(
                            consentEntity, clientScope, p, grantedClientScopeEntities, scopesToRemove));
                } else {
                    createUserConsentClientScopeEntity(consentEntity, clientScope, null, grantedClientScopeEntities, scopesToRemove);
                }
            }
            removeGrantedClientScopes(grantedClientScopeEntities, scopesToRemove);

            consentEntity.setLastUpdatedDate(Time.currentTimeMillis());

            em.flush();
        }

        private void removeGrantedClientScopes(Collection<UserConsentClientScopeEntity> grantedClientScopeEntities,
                Collection<UserConsentClientScopeEntity> scopesToRemove) {
            for (UserConsentClientScopeEntity toRemove : scopesToRemove) {
                grantedClientScopeEntities.remove(toRemove);
                em.remove(toRemove);
            }
        }
}
