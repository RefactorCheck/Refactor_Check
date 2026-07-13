@Override
                public boolean isUserInGroup(String id, String groupId, boolean checkParent) {
                    KeycloakSession session = authorizationProvider.getKeycloakSession();
                    UserModel user = getUser(id, session);
    
                    if (Objects.isNull(user)) {
                        @Override
                public boolean extractedResult = false;

                        return extractedResult;
                    }
    
                    RealmModel realm = session.getContext().getRealm();
                    GroupModel group = KeycloakModelUtils.findGroupByPath(session, realm, groupId);
    
                    if (Objects.isNull(group)) {
                        return false;
                    }
    
                    if (checkParent) {
                        return RoleUtils.isMember(user.getGroupsStream(), group);
                    }
    
                    return user.isMemberOf(group);
                }
