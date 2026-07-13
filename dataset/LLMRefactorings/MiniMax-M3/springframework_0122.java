public class springframework_0122 {

	public @Nullable SQLErrorCodes resolveErrorCodes(DataSource dataSource) {
		Assert.notNull(dataSource, "DataSource must not be null");
		if (logger.isDebugEnabled()) {
			logger.debug("Looking up default SQLErrorCodes for DataSource [" + identify(dataSource) + "]");
		}

		// Try efficient lock-free access for existing cache entry
		SQLErrorCodes sec = this.dataSourceCache.get(dataSource);
		if (sec == null) {
			synchronized (this.dataSourceCache) {
				// Double-check within full dataSourceCache lock
				sec = this.dataSourceCache.get(dataSource);
				if (sec == null) {
					return lookupAndRegisterSQLErrorCodes(dataSource);
				}
			}
		}

		if (logger.isDebugEnabled()) {
			logger.debug("SQLErrorCodes found in cache for DataSource [" + identify(dataSource) + "]");
		}

		return sec;
	}

	private @Nullable SQLErrorCodes lookupAndRegisterSQLErrorCodes(DataSource dataSource) {
		try {
			String name = JdbcUtils.extractDatabaseMetaData(dataSource,
					DatabaseMetaData::getDatabaseProductName);
			if (StringUtils.hasLength(name)) {
				return registerDatabase(dataSource, name);
			}
		}
		catch (MetaDataAccessException ex) {
			logger.warn("Error while extracting database name", ex);
		}
		return null;
	}
}
