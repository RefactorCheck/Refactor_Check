public class nacos_0217 {

    public synchronized DataSourceService getDataSource() {
        try {
            if (DatasourceConfiguration.isEmbeddedStorage()) {
                return getOrCreateLocalDataSourceService();
            } else {
                return getOrCreateBasicDataSourceService();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private synchronized DataSourceService getOrCreateLocalDataSourceService() {
        if (localDataSourceService == null) {
            localDataSourceService = new LocalDataSourceServiceImpl();
            localDataSourceService.init();
        }
        return localDataSourceService;
    }

    private synchronized DataSourceService getOrCreateBasicDataSourceService() {
        if (basicDataSourceService == null) {
            basicDataSourceService = new ExternalDataSourceServiceImpl();
            basicDataSourceService.init();
        }
        return basicDataSourceService;
    }
}
