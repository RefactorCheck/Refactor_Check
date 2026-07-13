public class nacos_0167 {

    private static final String[] INSERT_COLUMNS = {
        "type", "author", "name", "c_desc", "status", "version", "namespace_id",
        "storage", "publish_pipeline_info", "gmt_create@NOW()", "gmt_modified@NOW()"
    };

    private static final String INSERT_FAILED_MSG = "insert ai_resource_version failed, no generated key";

        @Override
        public long insert(AiResourceVersion version) {
            AiResourceVersionMapper mapper =
                mapperManager.findMapper(dataSourceService.getDataSourceType(),
                    TableConstant.AI_RESOURCE_VERSION);
            String sql = mapper.insert(Arrays.asList(INSERT_COLUMNS));

            KeyHolder keyHolder = new GeneratedKeyHolder();
            jt.update(connection -> {
                PreparedStatement ps = connection.prepareStatement(sql, new String[] {"id"});
                ps.setString(1, version.getType());
                ps.setString(2, version.getAuthor());
                ps.setString(3, version.getName());
                ps.setString(4, version.getDesc());
                ps.setString(5, version.getStatus());
                ps.setString(6, version.getVersion());
                ps.setString(7, normalizeNamespaceId(version.getNamespaceId()));
                ps.setString(8, version.getStorage());
                ps.setString(9, version.getPublishPipelineInfo());
                return ps;
            }, keyHolder);

            Number key = keyHolder.getKey();
            if (key == null) {
                throw new IllegalStateException(INSERT_FAILED_MSG);
            }
            return key.longValue();
        }
}
