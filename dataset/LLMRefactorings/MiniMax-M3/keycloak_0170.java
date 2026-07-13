public class keycloak_0170 {

        @Override
        public UserModel addUser(RealmModel realm, String id, String username, boolean addDefaultRoles, boolean addDefaultRequiredActions) {
            if (id == null) {
                id = KeycloakModelUtils.generateId();
            }
    
            UserEntity entity = new UserEntity();
            entity.setId(id);
            long now = Time.currentTimeMillis();
            entity.setCreatedTimestamp(now);
            entity.setLastModifiedTimestamp(now);
            entity.setUsername(username.toLowerCase());
            entity.setRealmId(realm.getId());
            em.persist(entity);
            if (!EntityManagers.isBatchMode()) {
                em.flush();
            }
            UserAdapter userModel = new UserAdapter(session, realm, em, entity);
    
            if (addDefaultRoles) {
                userModel.grantRole(realm.getDefaultRole());
    
                // No need to check if user has group as it's new user
                realm.getDefaultGroupsStream().forEach(userModel::joinGroupImpl);
            }
    
            if (addDefaultRequiredActions) {
                addDefaultRequiredActions(userModel, realm);
            }
    
            return userModel;
        }
    
        private void addDefaultRequiredActions(UserModel userModel, RealmModel realm) {
            realm.getRequiredActionProvidersStream()
                .filter(RequiredActionProviderModel::isEnabled)
                .filter(RequiredActionProviderModel::isDefaultAction)
                .map(RequiredActionProviderModel::getAlias)
                .forEach(userModel::addRequiredAction);
        }
}
