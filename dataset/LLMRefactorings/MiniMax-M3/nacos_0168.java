public class nacos_0168 {

        @Override
        public boolean updateMetaCas(String namespaceId, String name, String type,
            long expectedMetaVersion,
            AiResource newValue) {
            AiResourceMapper mapper = mapperManager.findMapper(dataSourceService.getDataSourceType(),
                TableConstant.AI_RESOURCE);
            
            String sql =
                "UPDATE ai_resource SET status=?, c_desc=?, biz_tags=?, ext=?, version_info=?, meta_version=meta_version+1, "
                    + "gmt_modified=" + mapper.getFunction("NOW()")
                    + " WHERE namespace_id=? AND name=? AND type=? AND meta_version=?";
            
            Object[] args = new Object[] {newValue.getStatus(), newValue.getDesc(),
                newValue.getBizTags(), newValue.getExt(),
                newValue.getVersionInfo(), normalizeNamespaceId(namespaceId), name, type,
                expectedMetaVersion};
            
            EmbeddedStorageContextHolder.addSqlContext(sql, args);
            Boolean success = databaseOperate.blockUpdate();
            if (success == null || !success) {
                return false;
            }
            long newMetaVersion = expectedMetaVersion + 1;
            AiResource updated = find(namespaceId, name, type);
            return updated != null && updated.getMetaVersion() == newMetaVersion;
        }
}
