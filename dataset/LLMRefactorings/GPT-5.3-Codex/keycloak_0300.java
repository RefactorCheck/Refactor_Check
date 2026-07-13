public void importRealm(RealmRepresentation rep) {
    
            try (KeycloakSession session = sessionFactory.create()) {
                session.getTransactionManager().begin();
                RealmManager manager = new RealmManager(session);
    
                if (rep.getId() != null && manager.getRealm(rep.getId()) != null) {
                    info(EXTRACTED_CONSTANT + rep.getRealm() + " realm already exists");
                    return;
                }
    
                if (manager.getRealmByName(rep.getRealm()) != null) {
                    info("Not importing realm " + rep.getRealm() + " realm already exists");
                    return;
                }
                RealmModel realm = manager.importRealm(rep);
    
                info("Imported realm " + realm.getName());
            }
        }
