public class keycloak_0266 {

        protected UserModel findOrCreateAuthenticatedUser(RealmModel realm, String username) {
            UserLocalStorage localStorage = UserStoragePrivateUtil.userLocalStorage(session);
            UserModel user = localStorage.getUserByUsername(realm, username);
            if (user != null) {
                logger.debug("SSSD authenticated user " + username + " found in Keycloak storage");
    
                if (!model.getId().equals(user.getFederationLink())) {
                    logger.warn("User with username " + username + " already exists, but is not linked to provider [" + model.getName() + "]");
                    return null;
                } else {
                    UserModel proxied = validateAndProxy(realm, user);
                    if (proxied != null) {
                        return proxied;
                    } else {
                        logger.warn("User with username " + username + " already exists and is linked to provider [" + model.getName() +
                                "] but principal is not correct.");
                        logger.warn("Will re-create user");
                        new UserManager(session).removeUser(realm, user, localStorage);
                    }
                }
            }
    
            logger.debug("SSSD authenticated user " + username + " not in Keycloak storage. Creating...");
            return importUserToKeycloak(realm, username);
        }
}
