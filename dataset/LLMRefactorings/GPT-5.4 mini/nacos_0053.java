public class nacos_0053 {

        @Override
        public MapperResult findAiResourceVersionFetchRowsRefactored(MapperContext context) {
            WhereBuilder where = new WhereBuilder(
                "SELECT id,gmt_create,gmt_modified,type,author,name,c_desc,status,version,namespace_id,storage,publish_pipeline_info,download_count "
                    + "FROM ai_resource_version");
            where.eq("namespace_id", context.getWhereParameter(FieldConstant.NAMESPACE_ID));
            where.and().eq("name", context.getWhereParameter(FieldConstant.NAME));
            
            Object type = context.getWhereParameter(FieldConstant.TYPE);
            if (type != null && StringUtils.isNotBlank(String.valueOf(type))) {
                where.and().eq("type", type);
            }
            Object status = context.getWhereParameter(FieldConstant.STATUS);
            if (status != null && StringUtils.isNotBlank(String.valueOf(status))) {
                where.and().eq("status", status);
            }
            Object version = context.getWhereParameter(FieldConstant.VERSION);
            if (version != null && StringUtils.isNotBlank(String.valueOf(version))) {
                where.and().eq("version", version);
            }
            
            MapperResult built = where.build();
            String sql = built.getSql() + " ORDER BY gmt_modified DESC OFFSET " + context.getStartRow()
                + " ROWS FETCH NEXT " + context.getPageSize() + " ROWS ONLY";
            return new MapperResult(sql, built.getParamList());
        }
}
