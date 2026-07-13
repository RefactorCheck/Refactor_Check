private static void updateGrantedConsentEntity(UserConsentEntity consentEntity, UserConsentModel consentModel) {
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
            // Those client scopes were no longer on consentModel and will be removed
            for (UserConsentClientScopeEntity toRemove : scopesToRemove) {
                grantedClientScopeEntities.remove(toRemove);
                em.remove(toRemove);
            }
    
            consentEntity.setLastUpdatedDate(Time.currentTimeMillis());
    
            em.flush();
        }
