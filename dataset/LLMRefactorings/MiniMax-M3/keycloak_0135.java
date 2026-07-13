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
                    handleStrategy(strategy, connection, schema, databaseUpdateFile, session, updater, true);
                }
            } else {
                handleStrategy(strategy, connection, schema, databaseUpdateFile, session, updater, false);
            }
        }
        
        private void handleStrategy(MigrationStrategy strategy, Connection connection, String schema, File databaseUpdateFile, KeycloakSession session, JpaUpdaterProvider updater, boolean empty) {
            switch (strategy) {
                case UPDATE:
                    update(connection, schema, session, updater);
                    break;
                case MANUAL:
                    export(connection, schema, databaseUpdateFile, session, updater);
                    String prefix = empty ? "Database not initialized, please initialize database with " : "Database not up-to-date, please migrate database with ";
                    throw new ServerStartupError(prefix + databaseUpdateFile.getAbsolutePath(), false);
                case VALIDATE:
                    String msg = empty ? "Database not initialized, please enable database initialization" : "Database not up-to-date, please enable database migration";
                    throw new ServerStartupError(msg, false);
            }
        }
}
