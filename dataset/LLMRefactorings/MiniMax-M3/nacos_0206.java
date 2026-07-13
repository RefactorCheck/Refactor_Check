public class nacos_0206 {

        public boolean correctUsage(String group, Timestamp gmtModified) {
            GroupCapacityMapper groupCapacityMapper =
                mapperManager.findMapper(dataSourceService.getDataSourceType(),
                    TableConstant.GROUP_CAPACITY);
            MapperResult mapperResult;
            MapperContext context = new MapperContext();
            context.putUpdateParameter(FieldConstant.GMT_MODIFIED, gmtModified);
            context.putWhereParameter(FieldConstant.GROUP_ID, group);
            if (CLUSTER.equals(group)) {
                mapperResult = groupCapacityMapper.updateUsage(context);
                return executeUpdate(mapperResult);
            } else {
                // Note: add "tenant_id = ''" condition.
                mapperResult = groupCapacityMapper.updateUsageByWhere(context);
                return executeUpdate(mapperResult);
            }
        }

        private boolean executeUpdate(MapperResult mapperResult) {
            try {
                return jdbcTemplate.update(mapperResult.getSql(),
                    mapperResult.getParamList().toArray()) == 1;
            } catch (CannotGetJdbcConnectionException e) {
                FATAL_LOG.error("[db-error]", e);
                throw e;
            }
        }
}
