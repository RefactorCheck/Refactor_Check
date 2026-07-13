public class nacos_0217 {


        public synchronized DataSourceService getDataSourceRefactored() {
            try {
                
                // Embedded storage is used by default in stand-alone mode
                // In cluster mode, external databases are used by default
                
                if (DatasourceConfiguration.isEmbeddedStorage()) {
                    if (localDataSourceService == null) {
                        localDataSourceService = new LocalDataSourceServiceImpl();
                        localDataSourceService.init();
                    }
                    return localDataSourceService;
                } else {
                    if (basicDataSourceService == null) {
                        basicDataSourceService = new ExternalDataSourceServiceImpl();
                        basicDataSourceService.init();
                    }
                    return basicDataSourceService;
                }
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        
        }
}
