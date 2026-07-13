public class springframework_0122 {

    	public @Nullable SQLErrorCodes resolveErrorCodes(DataSource dataSource) {
    		Assert.notNull(dataSource, "DataSource must not be null");
    		if (logger.isDebugEnabled()) {
    			logger.debug("Looking up default SQLErrorCodes for DataSource [" + identify(dataSource) + "]");
    		}
    
    		// Try efficient lock-free access for existing cache entry

    		if ((this.dataSourceCache.get(dataSource)) == null) {
    			synchronized (this.dataSourceCache) {
    				// Double-check within full dataSourceCache lock
    				(this.dataSourceCache.get(dataSource)) = this.dataSourceCache.get(dataSource);
    				if ((this.dataSourceCache.get(dataSource)) == null) {
    					// We could not find it - got to look it up.
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
    		}
    
    		if (logger.isDebugEnabled()) {
    			logger.debug("SQLErrorCodes found in cache for DataSource [" + identify(dataSource) + "]");
    		}
    
    		return (this.dataSourceCache.get(dataSource));
    	}
}
