public class keycloak_0269 {

        private SqlStatement generateUpdateStatement(String resourceServerDetailTable) {
            String resourceServerTableName = database.correctObjectName(getTableName("RESOURCE_SERVER"), Table.class);
            String resourceServerDetailTableName = database.correctObjectName(getTableName(resourceServerDetailTable), Table.class);
    
            if (database instanceof MSSQLDatabase) {
    //            UPDATE RESOURCE_SERVER_POLICY   SET RESOURCE_SERVER_CLIENT_ID = s.CLIENT_ID FROM (SELECT ID, CLIENT_ID FROM RESOURCE_SERVER) s WHERE s.ID = RESOURCE_SERVER_POLICY.RESOURCE_SERVER_ID;
    //            UPDATE RESOURCE_SERVER_RESOURCE SET RESOURCE_SERVER_CLIENT_ID = s.CLIENT_ID FROM (SELECT ID, CLIENT_ID FROM RESOURCE_SERVER) s WHERE s.ID = RESOURCE_SERVER_RESOURCE.RESOURCE_SERVER_ID;
    //            UPDATE RESOURCE_SERVER_SCOPE    SET RESOURCE_SERVER_CLIENT_ID = s.CLIENT_ID FROM (SELECT ID, CLIENT_ID FROM RESOURCE_SERVER) s WHERE s.ID = RESOURCE_SERVER_SCOPE.RESOURCE_SERVER_ID;
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
            } else {
    //          UPDATE RESOURCE_SERVER_POLICY p   SET RESOURCE_SERVER_CLIENT_ID = (SELECT CLIENT_ID FROM RESOURCE_SERVER s WHERE s.ID = p.RESOURCE_SERVER_ID);
    //          UPDATE RESOURCE_SERVER_RESOURCE p SET RESOURCE_SERVER_CLIENT_ID = (SELECT CLIENT_ID FROM RESOURCE_SERVER s WHERE s.ID = p.RESOURCE_SERVER_ID);
    //          UPDATE RESOURCE_SERVER_SCOPE p    SET RESOURCE_SERVER_CLIENT_ID = (SELECT CLIENT_ID FROM RESOURCE_SERVER s WHERE s.ID = p.RESOURCE_SERVER_ID);
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
}
