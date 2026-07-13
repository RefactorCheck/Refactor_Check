public class keycloak_0098 {

        @Override
        protected void generateStatementsImpl() throws CustomChangeException {
            String realmClientTableName = database.correctObjectName("REALM_CLIENT", Table.class);
    
            try {
                String trueValue = DataTypeFactory.getInstance().getTrueBooleanValue(database);
                PreparedStatement statement = jdbcConnection.prepareStatement("select CLIENT.REALM_ID, CLIENT.ID CLIENT_ID from " + getTableName("CLIENT") + " CLIENT where CLIENT.CONSENT_REQUIRED = " + trueValue);
    
                try {
                    ResultSet resultSet = statement.executeQuery();
                    try {
                        while (resultSet.next()) {
                            String realmId = resultSet.getString("REALM_ID");
                            String oauthClientId = resultSet.getString("CLIENT_ID");
    
                            InsertStatement realmClientInsert = new InsertStatement(null, null, realmClientTableName)
                                    .addColumnValue("REALM_ID", realmId)
                                    .addColumnValue("CLIENT_ID", oauthClientId);
                            statements.add(realmClientInsert);
                        }
                    } finally {
                        resultSet.close();
                    }
                } finally {
                    statement.close();
                }
    
                confirmationMessage.append("Inserted " + statements.size() + " OAuth Clients to REALM_CLIENT table");
            } catch (Exception e) {
                throw new CustomChangeException(getTaskId() + ": Exception when updating data from previous version", e);
            }
        }
}
