public class nacos_0119 {

        @Override
        public long insert(AiResource resource) {
            AiResourceMapper mapper = mapperManager.findMapper(dataSourceService.getDataSourceType(),
                TableConstant.AI_RESOURCE);
            String sql = mapper.insert(
                Arrays.asList("name", "type", "c_desc", "status", "namespace_id", "biz_tags", "ext",
                    "c_from", "version_info", "meta_version", "scope", "owner", "gmt_create@NOW()",
                    "gmt_modified@NOW()"));
            
            Object[] args = new Object[] {resource.getName(), resource.getType(), resource.getDesc(),
                resource.getStatus(),
                normalizeNamespaceId(resource.getNamespaceId()), resource.getBizTags(),
                resource.getExt(),
                resource.getFrom() == null ? "local" : resource.getFrom(),
                resource.getVersionInfo(),
                resource.getMetaVersion() == null ? 1L : resource.getMetaVersion(),
                resource.getScope() == null ? VisibilityConstants.SCOPE_PRIVATE
                    : resource.getScope(),
                resource.getOwner() == null ? "" : resource.getOwner()};
            
            EmbeddedStorageContextHolder.addSqlContext(sql, args);
            Boolean success = databaseOperate.blockUpdate();
            if (success == null || !success) {
                throw new IllegalStateException("insert ai_resource failed");
            }
            
            AiResource inserted =
                find(resource.getNamespaceId(), resource.getName(), resource.getType());
            if (inserted == null || inserted.getId() == null) {
                throw new IllegalStateException("insert ai_resource failed, cannot query inserted row");
            }
            return inserted.getId();
        }
}
