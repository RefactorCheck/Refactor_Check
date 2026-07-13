public class keycloak_0116 {

        @Override
        public void addConsent(RealmModel realm, String userId, UserConsentModel consent) {
            createIndex(realm, userId);
            String clientId = consent.getClient().getId();
    
            FederatedUserConsentEntity consentEntity = getGrantedConsentEntity(userId, clientId, LockModeType.NONE);
            if (consentEntity != null) {
                throw new ModelDuplicateException("Consent already exists for client [" + clientId + "] and user [" + userId + "]");
            }
    
            consentEntity = createConsentEntity(consent, userId, realm);
            em.persist(consentEntity);
            em.flush();
    
            updateGrantedConsentEntity(consentEntity, consent);
    
        }

        private FederatedUserConsentEntity createConsentEntity(UserConsentModel consent, String userId, RealmModel realm) {
            FederatedUserConsentEntity consentEntity = new FederatedUserConsentEntity();
            consentEntity.setId(KeycloakModelUtils.generateId());
            consentEntity.setUserId(userId);
            String clientId = consent.getClient().getId();
            StorageId clientStorageId = new StorageId(clientId);
            if (clientStorageId.isLocal()) {
                consentEntity.setClientId(clientId);
            } else {
                consentEntity.setClientStorageProvider(clientStorageId.getProviderId());
                consentEntity.setExternalClientId(clientStorageId.getExternalId());
            }
            consentEntity.setRealmId(realm.getId());
            consentEntity.setStorageProviderId(new StorageId(userId).getProviderId());
            long currentTime = Time.currentTimeMillis();
            consentEntity.setCreatedDate(currentTime);
            consentEntity.setLastUpdatedDate(currentTime);
            return consentEntity;
        }
}
