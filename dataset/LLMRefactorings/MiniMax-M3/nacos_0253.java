public class nacos_0253 {

    private static final int DEFAULT_QUERY_TIMEOUT_SECONDS = 3;
    private static final int MAX_ROWS_LIMIT = 50000;
    private static final int TEST_MASTER_WRITABLE_QUERY_TIMEOUT_SECONDS = 1;
    private static final int SCHEDULED_TASK_INITIAL_DELAY_SECONDS = 10;
    private static final int SCHEDULED_TASK_PERIOD_SECONDS = 10;

        @Override
        public void init() {
            queryTimeout = ConvertUtils.toInt(System.getProperty("QUERYTIMEOUT"), DEFAULT_QUERY_TIMEOUT_SECONDS);
            jt = new JdbcTemplate();
            // Set the maximum number of records to prevent memory expansion
            jt.setMaxRows(MAX_ROWS_LIMIT);
            jt.setQueryTimeout(queryTimeout);
            
            testMasterJt = new JdbcTemplate();
            testMasterJt.setQueryTimeout(queryTimeout);
            
            testMasterWritableJt = new JdbcTemplate();
            // Prevent the login interface from being too long because the main library is not available
            testMasterWritableJt.setQueryTimeout(TEST_MASTER_WRITABLE_QUERY_TIMEOUT_SECONDS);
            
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
                    PersistenceExecutor.scheduleTask(new SelectMasterTask(), SCHEDULED_TASK_INITIAL_DELAY_SECONDS, SCHEDULED_TASK_PERIOD_SECONDS, TimeUnit.SECONDS);
                }
                PersistenceExecutor.scheduleTask(new CheckDbHealthTask(), SCHEDULED_TASK_INITIAL_DELAY_SECONDS, SCHEDULED_TASK_PERIOD_SECONDS, TimeUnit.SECONDS);
            }
        }
}
