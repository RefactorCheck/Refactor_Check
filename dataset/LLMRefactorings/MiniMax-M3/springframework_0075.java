public class springframework_0075 {

	@Override
	public void initializeWithTableColumnMetaData(DatabaseMetaData databaseMetaData,
			@Nullable String catalogName, @Nullable String schemaName, @Nullable String tableName)
			throws SQLException {
	
		if (!this.includeSynonyms) {
			logger.debug("Defaulting to no synonyms in table meta-data lookup");
			super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
			return;
		}
	
		Connection con = databaseMetaData.getConnection();
		if (con == null) {
			logger.info("Unable to include synonyms in table meta-data lookup - no Connection from DatabaseMetaData");
			super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
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
			super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
			return;
		}
	
		logger.debug("Including synonyms in table meta-data lookup");
		Boolean originalValueForIncludeSynonyms = enableSynonyms(con);
	
		super.initializeWithTableColumnMetaData(databaseMetaData, catalogName, schemaName, tableName);
	
		resetSynonyms(con, originalValueForIncludeSynonyms);
	}

	private Boolean enableSynonyms(Connection con) {
		try {
			Method getIncludeSynonyms = con.getClass().getMethod("getIncludeSynonyms");
			ReflectionUtils.makeAccessible(getIncludeSynonyms);
			Boolean originalValueForIncludeSynonyms = (Boolean) getIncludeSynonyms.invoke(con);
	
			Method setIncludeSynonyms = con.getClass().getMethod("setIncludeSynonyms", boolean.class);
			ReflectionUtils.makeAccessible(setIncludeSynonyms);
			setIncludeSynonyms.invoke(con, Boolean.TRUE);
	
			return originalValueForIncludeSynonyms;
		}
		catch (Throwable ex) {
			throw new InvalidDataAccessApiUsageException("Could not prepare Oracle Connection", ex);
		}
	}

	private void resetSynonyms(Connection con, Boolean originalValueForIncludeSynonyms) {
		try {
			Method setIncludeSynonyms = con.getClass().getMethod("setIncludeSynonyms", boolean.class);
			ReflectionUtils.makeAccessible(setIncludeSynonyms);
			setIncludeSynonyms.invoke(con, originalValueForIncludeSynonyms);
		}
		catch (Throwable ex) {
			throw new InvalidDataAccessApiUsageException("Could not reset Oracle Connection", ex);
		}
	}
}
