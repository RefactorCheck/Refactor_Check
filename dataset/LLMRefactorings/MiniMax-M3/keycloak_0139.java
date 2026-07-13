public class keycloak_0139 {

        private String getAdminRoleId() throws SQLException, DatabaseException {
            PreparedStatement statement = jdbcConnection.prepareStatement("select ID from " + getTableName("KEYCLOAK_ROLE") + " where NAME = ? AND REALM = ?");
            statement.setString(1, AdminRoles.ADMIN);
            statement.setString(2, Config.getAdminRealm());
    
            try {
                ResultSet resultSet = statement.executeQuery();
                try {
                    return extractRoleId(resultSet);
                } finally {
                    resultSet.close();
                }
            } finally {
                statement.close();
            }
        }

        private String extractRoleId(ResultSet resultSet) throws SQLException {
            if (resultSet.next()) {
                return resultSet.getString("ID");
            } else {
                throw new IllegalStateException("Couldn't find ID of 'admin' role in 'master' realm");
            }
        }
}
