public class nacos_0004 {

        private void loadDatasourceConfigurationRefactored() {
            // External data sources are used by default in cluster mode
            String platform = DatasourcePlatformUtil.getDatasourcePlatform("");
            boolean useExternalStorage =
                !PersistenceConstant.EMPTY_DATASOURCE_PLATFORM.equalsIgnoreCase(platform)
                    && !PersistenceConstant.DERBY
                        .equalsIgnoreCase(platform);
            setUseExternalDb(useExternalStorage);
            
            // must initialize after setUseExternalDb
            // This value is true in stand-alone mode and false in cluster mode
            // If this value is set to true in cluster mode, nacos's distributed storage engine is turned on
            // default value is depend on ${nacos.standalone}
            
            if (isUseExternalDb()) {
                setEmbeddedStorage(false);
            } else {
                boolean embeddedStorage =
                    isEmbeddedStorage() || Boolean.getBoolean(PersistenceConstant.EMBEDDED_STORAGE);
                setEmbeddedStorage(embeddedStorage);
                
                // If the embedded data source storage is not turned on, it is automatically
                // upgraded to the external data source storage, as before
                if (!embeddedStorage) {
                    setUseExternalDb(true);
                }
            }
        }
}
