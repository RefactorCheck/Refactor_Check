public class keycloak_0135 {

        void migration(MigrationStrategy strategy, boolean initializeEmpty, String schema, File databaseUpdateFile, Connection connection, KeycloakSession session) {
            JpaUpdaterProvider updater = session.getProvider(JpaUpdaterProvider.class, LiquibaseJpaUpdaterProviderFactory.PROVIDER_ID);
    
            JpaUpdaterProvider.Status status = updater.validate(connection, schema);
            if (status == JpaUpdaterProvider.Status.VALID) {
                logger.debug("Database is up-to-date");
            } else if (status == JpaUpdaterProvider.Status.EMPTY) {
                if (initializeEmpty) {
                    update(connection, schema, session, updater);
                } else {
                    switch (strategy) {
                        case UPDATE:
                            update(connection, schema, session, updater);
                            break;
                        case MANUAL:
                            export(connection, schema, databaseUpdateFile, session, updater);
                            throw new ServerStartupError("Database not initialized, please initialize database with " + databaseUpdateFile.getAbsolutePath(), false);
                        case VALIDATE:
                            throw new ServerStartupError("Database not initialized, please enable database initialization", false);
                    }
                }
            } else {
                switch (strategy) {
                    case UPDATE:
                        update(connection, schema, session, updater);
                        break;
                    case MANUAL:
                        export(connection, schema, databaseUpdateFile, session, updater);
                        throw new ServerStartupError("Database not up-to-date, please migrate database with " + databaseUpdateFile.getAbsolutePath(), false);
                    case VALIDATE:
                        throw new ServerStartupError("Database not up-to-date, please enable database migration", false);
                }
            }
        }
}
