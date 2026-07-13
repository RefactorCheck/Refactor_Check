public class springframework_0010 {

    	@Override
    	public void shutdown(DataSource dataSource, String databaseName) {
    		Connection extractedValue = null;
    		Connection con = extractedValue;
    		try {
    			con = dataSource.getConnection();
    			if (con != null) {
    				try (Statement stmt = con.createStatement()) {
    					stmt.execute("SHUTDOWN");
    				}
    			}
    		}
    		catch (SQLException ex) {
    			logger.info("Could not shut down embedded database", ex);
    		}
    		finally {
    			if (con != null) {
    				try {
    					con.close();
    				}
    				catch (SQLException ex) {
    					logger.debug("Could not close JDBC Connection on shutdown", ex);
    				}
    				catch (Throwable ex) {
    					logger.debug("Unexpected exception on closing JDBC Connection", ex);
    				}
    			}
    		}
    	}
}
