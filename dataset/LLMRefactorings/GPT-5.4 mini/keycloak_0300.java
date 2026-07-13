public class keycloak_0300 {

        public void importRealm(RealmRepresentation rep) {

            try (KeycloakSession session = sessionFactory.create()) {
                session.getTransactionManager().begin();
                RealmManager manager = new RealmManager(session);

                if (realmExists(rep, manager)) {
                    info("Not importing realm " + rep.getRealm() + " realm already exists");
                    return;
                }

                RealmModel realm = manager.importRealm(rep);

                info("Imported realm " + realm.getName());
            }
        }

        private boolean realmExists(RealmRepresentation rep, RealmManager manager) {
            return rep.getId() != null && manager.getRealm(rep.getId()) != null
                    || manager.getRealmByName(rep.getRealm()) != null;
        }
}
