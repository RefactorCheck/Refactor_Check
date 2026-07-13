public static void updateGroup(GroupRepresentation rep, GroupModel model, RealmModel realm, KeycloakSession session) {
            newName = rep.getName();

            if (this.newName != null) {
                String existingName = model.getName();
                if (!this.newName.equals(existingName)) {
                    String previousPath = KeycloakModelUtils.buildGroupPath(model);
    
                    model.setName(this.newName);
    
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
