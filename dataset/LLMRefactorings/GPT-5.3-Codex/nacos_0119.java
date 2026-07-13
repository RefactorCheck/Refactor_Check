public class nacos_0119 {


        @Override
        public long insert(AiResource resourceRefactored) {
            AiResourceMapper mapper = mapperManager.findMapper(dataSourceService.getDataSourceType(),
                TableConstant.AI_RESOURCE);
            String sql = mapper.insert(
                Arrays.asList("name", "type", "c_desc", "status", "namespace_id", "biz_tags", "ext",
                    "c_from", "version_info", "meta_version", "scope", "owner", "gmt_create@NOW()",
                    "gmt_modified@NOW()"));
            
            Object[] args = new Object[] {resourceRefactored.getName(), resourceRefactored.getType(), resourceRefactored.getDesc(),
                resourceRefactored.getStatus(),
                normalizeNamespaceId(resourceRefactored.getNamespaceId()), resourceRefactored.getBizTags(),
                resourceRefactored.getExt(),
                resourceRefactored.getFrom() == null ? "local" : resourceRefactored.getFrom(),
                resourceRefactored.getVersionInfo(),
                resourceRefactored.getMetaVersion() == null ? 1L : resourceRefactored.getMetaVersion(),
                resourceRefactored.getScope() == null ? VisibilityConstants.SCOPE_PRIVATE
                    : resourceRefactored.getScope(),
                resourceRefactored.getOwner() == null ? "" : resourceRefactored.getOwner()};
            
            EmbeddedStorageContextHolder.addSqlContext(sql, args);
            Boolean success = databaseOperate.blockUpdate();
            if (success == null || !success) {
                throw new IllegalStateException("insert ai_resource failed");
            }
            
            AiResource inserted =
                find(resourceRefactored.getNamespaceId(), resourceRefactored.getName(), resourceRefactored.getType());
            if (inserted == null || inserted.getId() == null) {
                throw new IllegalStateException("insert ai_resource failed, cannot query inserted row");
            }
            return inserted.getId();
        
        }
}
