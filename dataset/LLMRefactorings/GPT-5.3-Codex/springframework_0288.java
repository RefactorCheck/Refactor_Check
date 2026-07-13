public class springframework_0288 {

    	@Override
    	protected long getNextKey() throws DataAccessException {
    		applyExtractedRefactoring();

    		Connection con = DataSourceUtils.getConnection(dataSource);
    		Statement stmt = null;
    		ResultSet rs = null;
    		try {
    			stmt = con.createStatement();
    			DataSourceUtils.applyTransactionTimeout(stmt, dataSource);
    			rs = stmt.executeQuery(getSequenceQuery());
    			if (rs.next()) {
    				return rs.getLong(1);
    			}
    			else {
    				throw new DataAccessResourceFailureException("Sequence query did not return a result");
    			}
    		}
    		catch (SQLException ex) {
    			throw new DataAccessResourceFailureException("Could not obtain sequence value", ex);
    		}
    		finally {
    			JdbcUtils.closeResultSet(rs);
    			JdbcUtils.closeStatement(stmt);
    			DataSourceUtils.releaseConnection(con, dataSource);
    		}
    	}

	private void applyExtractedRefactoring() {
    		DataSource dataSource = obtainDataSource();
	}
}
