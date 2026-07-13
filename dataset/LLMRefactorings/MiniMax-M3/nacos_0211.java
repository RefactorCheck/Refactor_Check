public class nacos_0211 {

        public static String initCacheDir(String namespace, NacosClientProperties properties) {
            
            String jmSnapshotPath = properties.getProperty(JM_SNAPSHOT_PATH_PROPERTY);
            String namingCacheRegistryDir = getNamingCacheRegistryDir(properties);
            
            if (!StringUtils.isBlank(jmSnapshotPath)) {
                cacheDir = jmSnapshotPath + File.separator + FILE_PATH_NACOS + namingCacheRegistryDir
                    + File.separator
                    + FILE_PATH_NAMING + File.separator + namespace;
            } else {
                cacheDir =
                    properties.getProperty(USER_HOME_PROPERTY) + File.separator + FILE_PATH_NACOS
                        + namingCacheRegistryDir
                        + File.separator + FILE_PATH_NAMING + File.separator + namespace;
            }
            
            return cacheDir;
        }

        private static String getNamingCacheRegistryDir(NacosClientProperties properties) {
            String registryDir = properties.getProperty(PropertyKeyConst.NAMING_CACHE_REGISTRY_DIR);
            if (registryDir != null) {
                return File.separator + registryDir;
            }
            return "";
        }
}
