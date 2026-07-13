public class springframework_0117 {

    public static void execute(DatabasePopulator populator, DataSource dataSource) throws DataAccessException {
        Assert.notNull(populator, "DatabasePopulator must not be null");
        Assert.notNull(dataSource, "DataSource must not be null");
        try {
            Connection connection = DataSourceUtils.getConnection(dataSource);
            try {
                populator.populate(connection);
                commitIfNecessary(connection, dataSource);
            }
            finally {
                DataSourceUtils.releaseConnection(connection, dataSource);
            }
        }
        catch (ScriptException ex) {
            throw ex;
        }
        catch (Throwable ex) {
            throw new UncategorizedScriptException("Failed to execute database script", ex);
        }
    }

    private static void commitIfNecessary(Connection connection, DataSource dataSource) throws SQLException {
        if (!connection.getAutoCommit() && !DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
            connection.commit();
        }
    }
}
