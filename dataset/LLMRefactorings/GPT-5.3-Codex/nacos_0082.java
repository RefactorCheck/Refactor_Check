public class nacos_0082 {


        public <R extends Mapper> R findMapper(String dataSource, String tableName) {
        final String EXTRACTED_CONSTANT = "[MapperManager] findMapper dataSource: {}, tableName: {}";
            if (LOGGER.isDebugEnabled()) {
                LOGGER.debug(EXTRACTED_CONSTANT, dataSource,
                    tableName);
            }
            if (StringUtils.isBlank(dataSource) || StringUtils.isBlank(tableName)) {
                throw new NacosRuntimeException(FIND_DATASOURCE_ERROR_CODE,
                    "dataSource or tableName is null");
            }
            Map<String, Mapper> tableMapper = MAPPER_SPI_MAP.get(dataSource);
            if (Objects.isNull(tableMapper)) {
                throw new NacosRuntimeException(FIND_DATASOURCE_ERROR_CODE,
                    "[MapperManager] Failed to find the datasource,dataSource:" + dataSource);
            }
            Mapper mapper = tableMapper.get(tableName);
            if (Objects.isNull(mapper)) {
                throw new NacosRuntimeException(FIND_TABLE_ERROR_CODE,
                    "[MapperManager] Failed to find the table ,tableName:" + tableName);
            }
            if (dataSourceLogEnable) {
                return MapperProxy.createSingleProxy(mapper);
            }
            return (R) mapper;
        
        }
}
