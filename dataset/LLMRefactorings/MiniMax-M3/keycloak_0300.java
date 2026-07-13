public class keycloak_0300 {

        public void importRealm(RealmRepresentation rep) {
    
            try (KeycloakSession session = sessionFactory.create()) {
                session.getTransactionManager().begin();
                RealmManager manager = new RealmManager(session);
    
                if (realmAlreadyExists(rep, manager)) {
                    return;
                }
    
                RealmModel realm = manager.importRealm(rep);
    
                info("Imported realm " + realm.getName());
            }
        }

        private boolean realmAlreadyExists(RealmRepresentation rep, RealmManager manager) {
            String realmName = rep.getRealm();
            if (rep.getId() != null && manager.getRealm(rep.getId()) != null) {
                info("Not importing realm " + realmName + " realm already exists");
                return true;
            }
            if (manager.getRealmByName(realmName) != null) {
                info("Not importing realm " + realmName + " realm already exists");
                return true;
            }
            return false;
        }
}
