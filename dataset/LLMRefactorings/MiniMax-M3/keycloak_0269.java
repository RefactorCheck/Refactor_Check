public class keycloak_0269 {

        private SqlStatement generateUpdateStatement(String resourceServerDetailTable) {
            String resourceServerTableName = database.correctObjectName(getTableName("RESOURCE_SERVER"), Table.class);
            String resourceServerDetailTableName = database.correctObjectName(getTableName(resourceServerDetailTable), Table.class);
    
            if (database instanceof MSSQLDatabase) {
                return buildMssqlUpdateStatement(resourceServerTableName, resourceServerDetailTableName);
            } else {
                return buildStandardUpdateStatement(resourceServerTableName, resourceServerDetailTableName);
            }
        }

        private SqlStatement buildMssqlUpdateStatement(String resourceServerTableName, String resourceServerDetailTableName) {
            return new RawSqlStatement(
              "UPDATE "
              + resourceServerDetailTableName
                + " SET RESOURCE_SERVER_CLIENT_ID = s.CLIENT_ID FROM "
                  + " (SELECT ID, CLIENT_ID FROM "
                  + resourceServerTableName
                  + ") s "
                + " WHERE s.ID = "
                + resourceServerDetailTableName
                + ".RESOURCE_SERVER_ID"
            );
        }

        private SqlStatement buildStandardUpdateStatement(String resourceServerTableName, String resourceServerDetailTableName) {
            return new RawSqlStatement(
              "UPDATE "
              + resourceServerDetailTableName
              + " p SET RESOURCE_SERVER_CLIENT_ID = "
                + "(SELECT CLIENT_ID FROM "
                + resourceServerTableName
                + " s WHERE s.ID = p.RESOURCE_SERVER_ID)"
            );
        }
}
