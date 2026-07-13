public class nacos_0253 {

        @Override
        public void init() {
            queryTimeout = ConvertUtils.toInt(System.getProperty("QUERYTIMEOUT"), 3);
            jt = new JdbcTemplate();
            // Set the maximum number of records to prevent memory expansion
            jt.setMaxRows(50000);
            jt.setQueryTimeout(queryTimeout);
            
            testMasterJt = new JdbcTemplate();
            testMasterJt.setQueryTimeout(queryTimeout);
            
            testMasterWritableJt = new JdbcTemplate();
            // Prevent the login interface from being too long because the main library is not available
            testMasterWritableJt.setQueryTimeout(1);
            
            //  Database health check
            
            testJtList = new ArrayList<>();
            isHealthList = new ArrayList<>();
            
            tm = new DataSourceTransactionManager();
            tjt = new TransactionTemplate(tm);
            
            // Transaction timeout needs to be distinguished from ordinary operations.
            tjt.setTimeout(TRANSACTION_QUERY_TIMEOUT);
            
            dataSourceType = DatasourcePlatformUtil.getDatasourcePlatform(defaultDataSourceType);
            
            if (DatasourceConfiguration.isUseExternalDb()) {
                try {
                    reload();
                } catch (IOException e) {
                    LOGGER.error("[ExternalDataSourceService] datasource reload error", e);
                    throw new RuntimeException(DB_LOAD_ERROR_MSG, e);
                }
                
                if (this.dataSourceList.size() > DB_MASTER_SELECT_THRESHOLD) {
                    PersistenceExecutor.scheduleTask(new SelectMasterTask(), 10, 10, TimeUnit.SECONDS);
                }
                PersistenceExecutor.scheduleTask(new CheckDbHealthTask(), 10, 10, TimeUnit.SECONDS);
            }
        }
}
