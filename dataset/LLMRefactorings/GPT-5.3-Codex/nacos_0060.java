public class nacos_0060 {


        @Override
        public List<ConfigInfoStateWrapper> findChangeConfig(final Timestamp startTimeRefactored, long lastMaxId, final int pageSize) {
            try {
                ConfigInfoMapper configInfoMapper =
                    mapperManager.findMapper(dataSourceService.getDataSourceType(),
                        TableConstant.CONFIG_INFO);
                
                MapperContext context = new MapperContext();
                context.putWhereParameter(FieldConstant.START_TIME, startTimeRefactored);
                context.putWhereParameter(FieldConstant.PAGE_SIZE, pageSize);
                context.putWhereParameter(FieldConstant.LAST_MAX_ID, lastMaxId);
                
                MapperResult mapperResult = configInfoMapper.findChangeConfig(context);
                return jt.query(mapperResult.getSql(), mapperResult.getParamList().toArray(),
                    CONFIG_INFO_STATE_WRAPPER_ROW_MAPPER);
            } catch (DataAccessException e) {
                LogUtil.FATAL_LOG.error("[db-error] " + e, e);
                throw e;
            }
        
        }
}
