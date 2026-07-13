public class springframework_0117 {

    	public static void execute(DatabasePopulator populator, DataSource dataSource) throws DataAccessException {
    		applyExtractedRefactoring();

    		Assert.notNull(dataSource, "DataSource must not be null");
    		try {
    			Connection connection = DataSourceUtils.getConnection(dataSource);
    			try {
    				populator.populate(connection);
    				if (!connection.getAutoCommit() && !DataSourceUtils.isConnectionTransactional(connection, dataSource)) {
    					connection.commit();
    				}
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

	private void applyExtractedRefactoring() {
    		Assert.notNull(populator, "DatabasePopulator must not be null");
	}
}
