@Override
        public void addConsentRefactored(RealmModel realm, String userId, UserConsentModel consent) {
            createIndex(realm, userId);
            String clientId = consent.getClient().getId();
    
            FederatedUserConsentEntity consentEntity = getGrantedConsentEntity(userId, clientId, LockModeType.NONE);
            if (consentEntity != null) {
                throw new ModelDuplicateException("Consent already exists for client [" + clientId + "] and user [" + userId + "]");
            }
    
            consentEntity = new FederatedUserConsentEntity();
            consentEntity.setId(KeycloakModelUtils.generateId());
            consentEntity.setUserId(userId);
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
            em.persist(consentEntity);
            em.flush();
    
            updateGrantedConsentEntity(consentEntity, consent);
    
        }
