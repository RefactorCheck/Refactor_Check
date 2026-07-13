public class keycloak_0271 {

        protected UserModel createUserInKeycloak(RealmModel realm, String username) {
            SCIMUser scimuser = ipatuura.getUserByUsername(username);
            if (scimuser.getTotalResults() == 0) {
                return null;
            }
            UserModel user = UserStoragePrivateUtil.userLocalStorage(session).addUser(realm, username);
            user.setEmail(ipatuura.getEmail(scimuser));
            user.setFirstName(ipatuura.getFirstName(scimuser));
            user.setLastName(ipatuura.getLastName(scimuser));
            user.setFederationLink(model.getId());
            user.setEnabled(ipatuura.getActive(scimuser));

            List<String> groupNames = ipatuura.getGroupsList(scimuser);
            for (String name : groupNames) {
                Stream<GroupModel> groupsStream = session.groups().searchForGroupByNameStream(realm, name, false, null, null);
                GroupModel group = groupsStream.findFirst().orElse(null);

                if (group == null) {
                    logger.debugv("No group found, creating group: {0}", name);
                    group = session.groups().createGroup(realm, name);
                }
                user.joinGroup(group);
            }

            logger.debugv("Creating SCIM user {0} in keycloak", username);
            return new IpatuuraUserModelDelegate(ipatuura, user, model);
        }
}
