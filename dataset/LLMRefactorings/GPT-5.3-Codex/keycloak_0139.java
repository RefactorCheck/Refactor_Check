private String getAdminRoleId() throws SQLException, DatabaseException {
            PreparedStatement adminRoleStatement = jdbcConnection.prepareStatement("select ID from " + getTableName("KEYCLOAK_ROLE") + " where NAME = ? AND REALM = ?");
            adminRoleStatement.setString(1, AdminRoles.ADMIN);
            adminRoleStatement.setString(2, Config.getAdminRealm());
    
            try {
                ResultSet resultSet = adminRoleStatement.executeQuery();
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
                adminRoleStatement.close();
            }
        }
