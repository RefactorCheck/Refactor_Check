public class keycloak_0151 {

        @Override
        protected void generateStatementsImpl() throws CustomChangeException {
            final String userConsentClientScopeTable = getTableName("USER_CONSENT_CLIENT_SCOPE");
            final String userConsentTable = getTableName("USER_CONSENT");
            statements.add(new RawSqlStatement(
                    "DELETE FROM "+ userConsentClientScopeTable + " WHERE USER_CONSENT_ID IN (" +
                            " SELECT uc.ID FROM "+userConsentTable+" uc INNER JOIN (" +
                            " SELECT CLIENT_ID, USER_ID, MAX(LAST_UPDATED_DATE) AS MAX_UPDATED_DATE FROM " + userConsentTable +
                            " GROUP BY CLIENT_ID, USER_ID HAVING COUNT(*) > 1 ) max_dates ON uc.CLIENT_ID = max_dates.CLIENT_ID" +
                            " AND uc.USER_ID = max_dates.USER_ID AND uc.LAST_UPDATED_DATE = max_dates.MAX_UPDATED_DATE)"
            ));
            statements.add(new RawSqlStatement(
                    "CREATE TABLE TEMP_USER_CONSENT_IDS (ID VARCHAR(36) NOT NULL, PRIMARY KEY (ID))"
            ));
            statements.add(new RawSqlStatement(
                    "INSERT INTO TEMP_USER_CONSENT_IDS SELECT uc.ID FROM " + userConsentTable + " uc INNER JOIN (" +
                            " SELECT CLIENT_ID, USER_ID, MAX(LAST_UPDATED_DATE) AS MAX_UPDATED_DATE" +
                            " FROM "+userConsentTable+" GROUP BY CLIENT_ID, USER_ID HAVING COUNT(*) > 1 )" +
                            " max_dates ON uc.CLIENT_ID = max_dates.CLIENT_ID" +
                            " AND uc.USER_ID = max_dates.USER_ID AND uc.LAST_UPDATED_DATE = max_dates.MAX_UPDATED_DATE"
            ));
            statements.add(new DeleteStatement(null, null, database.correctObjectName("USER_CONSENT", Table.class))
                    .setWhere("ID IN (SELECT ID FROM TEMP_USER_CONSENT_IDS)"));
            statements.add(new RawSqlStatement("DROP TABLE IF EXISTS TEMP_USER_CONSENT_IDS"));
    
            statements.add(new RawSqlStatement(
                    " DELETE FROM "+ userConsentClientScopeTable + " WHERE USER_CONSENT_ID IN (" +
                            " SELECT uc.ID FROM "+userConsentTable+" uc INNER JOIN (" +
                            " SELECT CLIENT_STORAGE_PROVIDER, EXTERNAL_CLIENT_ID, USER_ID, MAX(LAST_UPDATED_DATE) AS MAX_UPDATED_DATE" +
                            " FROM "+userConsentTable+" GROUP BY CLIENT_STORAGE_PROVIDER, EXTERNAL_CLIENT_ID, USER_ID HAVING COUNT(*) > 1 )" +
                            " max_dates ON uc.CLIENT_STORAGE_PROVIDER = max_dates.CLIENT_STORAGE_PROVIDER" +
                            " AND uc.EXTERNAL_CLIENT_ID = max_dates.EXTERNAL_CLIENT_ID AND uc.USER_ID = max_dates.USER_ID AND uc.LAST_UPDATED_DATE = max_dates.MAX_UPDATED_DATE )"
            ));
            statements.add(new RawSqlStatement(
                    "CREATE TABLE TEMP_USER_CONSENT_IDS2 (ID VARCHAR(36) NOT NULL, PRIMARY KEY (ID))"
            ));
            statements.add(new RawSqlStatement(
                    "INSERT INTO TEMP_USER_CONSENT_IDS2 SELECT uc.ID FROM " + userConsentTable + " uc INNER JOIN (" +
                            " SELECT CLIENT_STORAGE_PROVIDER, EXTERNAL_CLIENT_ID, USER_ID, MAX(LAST_UPDATED_DATE) AS MAX_UPDATED_DATE" +
                            " FROM "+userConsentTable+" GROUP BY CLIENT_STORAGE_PROVIDER, EXTERNAL_CLIENT_ID, USER_ID HAVING COUNT(*) > 1 )" +
                            " max_dates ON uc.CLIENT_STORAGE_PROVIDER = max_dates.CLIENT_STORAGE_PROVIDER" +
                            " AND uc.EXTERNAL_CLIENT_ID = max_dates.EXTERNAL_CLIENT_ID AND uc.USER_ID = max_dates.USER_ID AND uc.LAST_UPDATED_DATE = max_dates.MAX_UPDATED_DATE"
            ));
            statements.add(new DeleteStatement(null, null, database.correctObjectName("USER_CONSENT", Table.class))
                    .setWhere("ID IN (SELECT ID FROM TEMP_USER_CONSENT_IDS2)"));
            statements.add(new RawSqlStatement("DROP TABLE IF EXISTS TEMP_USER_CONSENT_IDS2"));
        }
}
