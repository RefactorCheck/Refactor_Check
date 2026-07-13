public class keycloak_0139 {

        private String getAdminRoleId() throws SQLException, DatabaseException {
            PreparedStatement selectStatement = jdbcConnection.prepareStatement("select ID from " + getTableName("KEYCLOAK_ROLE") + " where NAME = ? AND REALM = ?");
            selectStatement.setString(1, AdminRoles.ADMIN);
            selectStatement.setString(2, Config.getAdminRealm());
    
            try {
                ResultSet resultSet = selectStatement.executeQuery();
                try {
                    if (resultSet.next()) {
                        return resultSet.getString("ID");
                    } else {
                        throw new IllegalStateException("Couldn't find ID of 'admin' role in 'master' realm");
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                selectStatement.close();
            }
        }
}
