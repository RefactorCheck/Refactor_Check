public class nacos_0104 {

        @Override
        public boolean updateSourceCas(String namespaceId, String name, String type,
            long expectedMetaVersion,
            String source) {
            AiResourceMapper mapper = mapperManager.findMapper(dataSourceService.getDataSourceType(),
                TableConstant.AI_RESOURCE);
            
            String sql = "UPDATE ai_resource SET c_from=?, meta_version=meta_version+1, "
                + "gmt_modified=" + mapper.getFunction("NOW()")
                + " WHERE namespace_id=? AND name=? AND type=? AND meta_version=?";
            
            Object[] args = new Object[] {source, normalizeNamespaceId(namespaceId), name, type,
                expectedMetaVersion};
            
            EmbeddedStorageContextHolder.addSqlContext(sql, args);
            Boolean success = databaseOperate.blockUpdate();
            if (success == null || !success) {
                return false;
            }
            AiResource updated = find(namespaceId, name, type);
            return verifyMetaVersion(updated, expectedMetaVersion);
        }

        private boolean verifyMetaVersion(AiResource updated, long expectedMetaVersion) {
            return updated != null && updated.getMetaVersion() == expectedMetaVersion + 1;
        }
}
