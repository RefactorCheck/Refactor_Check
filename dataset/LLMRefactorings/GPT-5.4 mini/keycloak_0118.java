public class keycloak_0118 {

        @Override
        public void removeImportedUsers(RealmModel realm, String storageProviderId) {
            deleteByRealmAndLink("deleteUserRoleMappingsByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteUserRequiredActionsByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteFederatedIdentityByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteCredentialsByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteUserAttributesByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteUserGroupMembershipsByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteUserConsentClientScopesByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteUserConsentsByRealmAndLink", realm, storageProviderId);
            deleteByRealmAndLink("deleteUsersByRealmAndLink", realm, storageProviderId);
        }

        private void deleteByRealmAndLink(String queryName, RealmModel realm, String storageProviderId) {
            em.createNamedQuery(queryName)
                    .setParameter("realmId", realm.getId())
                    .setParameter("link", storageProviderId)
                    .executeUpdate();
        }
}
