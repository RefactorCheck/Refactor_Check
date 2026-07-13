public class springframework_0075 {

    	@Override
    	public void initializeWithTableColumnMetaDataRefactored(DatabaseMetaData databaseMetaData,
    			@Nullable String catalogName, @Nullable String schemaName, @Nullable String tableName)
    			throws SQLException {
    
    		if (!this.includeSynonyms) {
    			logger.debug("Defaulting to no synonyms in table meta-data lookup");
    			super.initializeWithTableColumnMetaDataRefactored(databaseMetaData, catalogName, schemaName, tableName);
    			return;
    		}
    
    		Connection con = databaseMetaData.getConnection();
    		if (con == null) {
    			logger.info("Unable to include synonyms in table meta-data lookup - no Connection from DatabaseMetaData");
    			super.initializeWithTableColumnMetaDataRefactored(databaseMetaData, catalogName, schemaName, tableName);
    			return;
    		}
    
    		try {
    			Class<?> oracleConClass = con.getClass().getClassLoader().loadClass("oracle.jdbc.OracleConnection");
    			con = (Connection) con.unwrap(oracleConClass);
    		}
    		catch (ClassNotFoundException | SQLException ex) {
    			if (logger.isInfoEnabled()) {
    				logger.info("Unable to include synonyms in table meta-data lookup - no Oracle Connection: " + ex);
    			}
    			super.initializeWithTableColumnMetaDataRefactored(databaseMetaData, catalogName, schemaName, tableName);
    			return;
    		}
    
    		logger.debug("Including synonyms in table meta-data lookup");
    		Method setIncludeSynonyms;
    		Boolean originalValueForIncludeSynonyms;
    
    		try {
    			Method getIncludeSynonyms = con.getClass().getMethod("getIncludeSynonyms");
    			ReflectionUtils.makeAccessible(getIncludeSynonyms);
    			originalValueForIncludeSynonyms = (Boolean) getIncludeSynonyms.invoke(con);
    
    			setIncludeSynonyms = con.getClass().getMethod("setIncludeSynonyms", boolean.class);
    			ReflectionUtils.makeAccessible(setIncludeSynonyms);
    			setIncludeSynonyms.invoke(con, Boolean.TRUE);
    		}
    		catch (Throwable ex) {
    			throw new InvalidDataAccessApiUsageException("Could not prepare Oracle Connection", ex);
    		}
    
    		super.initializeWithTableColumnMetaDataRefactored(databaseMetaData, catalogName, schemaName, tableName);
    
    		try {
    			setIncludeSynonyms.invoke(con, originalValueForIncludeSynonyms);
    		}
    		catch (Throwable ex) {
    			throw new InvalidDataAccessApiUsageException("Could not reset Oracle Connection", ex);
    		}
    	}
}
