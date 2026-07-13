public class keycloak_0220 {

        public static void updateGroup(GroupRepresentation rep, GroupModel model, RealmModel realm, KeycloakSession session) {
            String newName = rep.getName();
            if (newName != null) {
                String existingName = model.getName();
                if (!newName.equals(existingName)) {
                    String previousPath = KeycloakModelUtils.buildGroupPath(model);
    
                    model.setName(newName);
    
                    String newPath = KeycloakModelUtils.buildGroupPath(model);
    
                    GroupPathChangeEvent.fire(model, newPath, previousPath, session);
                }
            }
    
            if (rep.getAttributes() != null) {
                Set<String> attrsToRemove = new HashSet<>(model.getAttributes().keySet());
                attrsToRemove.removeAll(rep.getAttributes().keySet());
                for (Map.Entry<String, List<String>> attr : rep.getAttributes().entrySet()) {
                    model.setAttribute(attr.getKey(), attr.getValue());
                }
    
                for (String attr : attrsToRemove) {
                    model.removeAttribute(attr);
                }
            }
    
            model.setDescription(rep.getDescription());
        }
}
