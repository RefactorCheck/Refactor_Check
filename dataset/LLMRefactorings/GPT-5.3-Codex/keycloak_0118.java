@Override
        public void removeImportedUsers(RealmModel realm, String storageProviderId) {
            em.createNamedQuery(EXTRACTED_CONSTANT)
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteUserRequiredActionsByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteFederatedIdentityByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteCredentialsByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteUserAttributesByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteUserGroupMembershipsByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteUserConsentClientScopesByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteUserConsentsByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
            em.createNamedQuery("deleteUsersByRealmAndLink")
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
        }
