public class nacos_0174 {

        @Override
        public MapperResult findAiResourceVersionFetchRows(MapperContext context) {
            WhereBuilder where = new WhereBuilder(
                "SELECT id,gmt_create,gmt_modified,type,author,name,c_desc,status,version,namespace_id,storage,publish_pipeline_info,download_count "
                    + "FROM ai_resource_version");
            where.eq("namespace_id", context.getWhereParameter(FieldConstant.NAMESPACE_ID));
            where.and().eq("name", context.getWhereParameter(FieldConstant.NAME));

            addOptionalCondition(where, "type", FieldConstant.TYPE, context);
            addOptionalCondition(where, "status", FieldConstant.STATUS, context);
            addOptionalCondition(where, "version", FieldConstant.VERSION, context);

            MapperResult built = where.build();
            String sql = built.getSql() + " ORDER BY gmt_modified DESC OFFSET " + context.getStartRow()
                + " ROWS FETCH NEXT " + context.getPageSize() + " ROWS ONLY";
            return new MapperResult(sql, built.getParamList());
        }

        private void addOptionalCondition(WhereBuilder where, String column, String fieldName, MapperContext context) {
            Object value = context.getWhereParameter(fieldName);
            if (value != null && StringUtils.isNotBlank(String.valueOf(value))) {
                where.and().eq(column, value);
            }
        }
}
