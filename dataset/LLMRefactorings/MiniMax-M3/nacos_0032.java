public class nacos_0032 {

        @Override
        public MapperResult findAiResourceVersionFetchRows(MapperContext context) {
            WhereBuilder where = new WhereBuilder(
                "SELECT id,gmt_create,gmt_modified,type,author,name,c_desc,status,version,namespace_id,storage,publish_pipeline_info,download_count "
                    + "FROM ai_resource_version");
            where.eq("namespace_id", context.getWhereParameter(FieldConstant.NAMESPACE_ID));
            where.and().eq("name", context.getWhereParameter(FieldConstant.NAME));

            addOptionalCondition(where, context, FieldConstant.TYPE);
            addOptionalCondition(where, context, FieldConstant.STATUS);
            addOptionalCondition(where, context, FieldConstant.VERSION);

            MapperResult built = where.build();
            String sql = built.getSql() + " ORDER BY gmt_modified DESC LIMIT " + context.getPageSize()
                + " OFFSET "
                + context.getStartRow();
            return new MapperResult(sql, built.getParamList());
        }

        private void addOptionalCondition(WhereBuilder where, MapperContext context, String fieldName) {
            Object value = context.getWhereParameter(fieldName);
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                where.and().eq(fieldName, value);
            }
        }
}
