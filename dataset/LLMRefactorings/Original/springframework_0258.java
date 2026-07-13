public class springframework_0258 {

    	@Override
    	public int[] batchUpdate(String sql, SqlParameterSource[] batchArgs) {
    		if (batchArgs.length == 0) {
    			return new int[0];
    		}
    
    		ParsedSql parsedSql = getParsedSql(sql);
    		PreparedStatementCreatorFactory pscf = getPreparedStatementCreatorFactory(parsedSql, batchArgs[0]);
    
    		return getJdbcOperations().batchUpdate(
    				pscf.getSql(),
    				new BatchPreparedStatementSetter() {
    					@Override
    					public void setValues(PreparedStatement ps, int i) throws SQLException {
    						@Nullable Object[] values = NamedParameterUtils.buildValueArray(parsedSql, batchArgs[i], null);
    						pscf.newPreparedStatementSetter(values).setValues(ps);
    					}
    					@Override
    					public int getBatchSize() {
    						return batchArgs.length;
    					}
    				});
    	}
}
