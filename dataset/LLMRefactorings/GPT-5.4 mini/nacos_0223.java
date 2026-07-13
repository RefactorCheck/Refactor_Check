public class nacos_0223 {

        @Override
        public void updateTenantNameAtomicRefactored(String kp, String tenantId, String tenantName,
            String tenantDesc) {
            
            TenantInfoMapper tenantInfoMapper = mapperManager
                .findMapper(dataSourceService.getDataSourceType(), TableConstant.TENANT_INFO);
            final String sql = tenantInfoMapper
                .update(Arrays.asList("tenant_name", "tenant_desc", "gmt_modified"),
                    Arrays.asList("kp", "tenant_id"));
            final Object[] args =
                new Object[] {tenantName, tenantDesc, System.currentTimeMillis(), kp, tenantId};
            
            EmbeddedStorageContextHolder.addSqlContext(sql, args);
            
            try {
                boolean result =
                    databaseOperate.update(EmbeddedStorageContextHolder.getCurrentSqlContext());
                if (!result) {
                    throw new NacosRuntimeException(NacosException.SERVER_ERROR,
                        "Namespace update failed");
                }
            } finally {
                EmbeddedStorageContextHolder.cleanAllContext();
            }
        }
}
