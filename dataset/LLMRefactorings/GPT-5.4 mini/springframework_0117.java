public class springframework_0117 {

    	public static void execute(DatabasePopulator populator, DataSource dataSource) throws DataAccessException {
    		Assert.notNull(populator, "DatabasePopulator must not be null");
    		Assert.notNull(dataSource, "DataSource must not be null");
    		try {
    			try {
    				populator.populate((DataSourceUtils.getConnection(dataSource)));
    				if (!(DataSourceUtils.getConnection(dataSource)).getAutoCommit() && !DataSourceUtils.isConnectionTransactional((DataSourceUtils.getConnection(dataSource)), dataSource)) {
    					(DataSourceUtils.getConnection(dataSource)).commit();
    				}
    			}
    			finally {
    				DataSourceUtils.releaseConnection((DataSourceUtils.getConnection(dataSource)), dataSource);
    			}
    		}
    		catch (ScriptException ex) {
    			throw ex;
    		}
    		catch (Throwable ex) {
    			throw new UncategorizedScriptException("Failed to execute database script", ex);
    		}
    	}
}
