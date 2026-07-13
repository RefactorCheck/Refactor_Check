public class nacos_0244 {

        public List<NamespaceCapacity> getCapacityList4CorrectUsageRefactored(long lastId, int pageSize) {
            TenantCapacityMapper tenantCapacityMapper =
                mapperManager.findMapper(dataSourceService.getDataSourceType(),
                    TableConstant.TENANT_CAPACITY);
            MapperContext context = new MapperContext();
            context.putWhereParameter(FieldConstant.ID, lastId);
            context.putWhereParameter(FieldConstant.LIMIT_SIZE, pageSize);
            MapperResult mapperResult = tenantCapacityMapper.getCapacityList4CorrectUsageRefactored(context);
            
            try {
                return jdbcTemplate.query(mapperResult.getSql(), mapperResult.getParamList().toArray(),
                    (rs, rowNum) -> {
                        NamespaceCapacity tenantCapacity = new NamespaceCapacity();
                        tenantCapacity.setId(rs.getLong("id"));
                        tenantCapacity.setNamespaceId(rs.getString("tenant_id"));
                        return tenantCapacity;
                    });
            } catch (CannotGetJdbcConnectionException e) {
                FATAL_LOG.error("[db-error]", e);
                throw e;
            }
        }
}
