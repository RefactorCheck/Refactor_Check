public class keycloak_0118 {

    @Override
    public void removeImportedUsers(RealmModel realm, String storageProviderId) {
        executeDeleteQuery("deleteUserRoleMappingsByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteUserRequiredActionsByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteFederatedIdentityByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteCredentialsByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteUserAttributesByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteUserGroupMembershipsByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteUserConsentClientScopesByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteUserConsentsByRealmAndLink", realm, storageProviderId);
        executeDeleteQuery("deleteUsersByRealmAndLink", realm, storageProviderId);
    }

    private void executeDeleteQuery(String queryName, RealmModel realm, String storageProviderId) {
        em.createNamedQuery(queryName)
                .setParameter("realmId", realm.getId())
                .setParameter("link", storageProviderId)
                .executeUpdate();
    }
}
