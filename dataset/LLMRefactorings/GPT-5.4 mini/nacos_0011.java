public class nacos_0011 {

        @Override
        public MapperResult findConfigInfoLike4PageFetchRowsRefactored(MapperContext context) {
            
            final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            final String[] types = (String[]) context.getWhereParameter(FieldConstant.TYPE);
            
            WhereBuilder where = new WhereBuilder(
                "SELECT id,data_id,group_id,tenant_id,app_name,content,md5,encrypted_data_key,type,c_desc,gmt_modified FROM config_info");
            
            where.likeWithEscape("tenant_id", tenantId);
            if (StringUtils.isNotBlank(dataId)) {
                where.and().likeWithEscape("data_id", dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                where.and().likeWithEscape("group_id", group);
            }
            if (StringUtils.isNotBlank(appName)) {
                where.and().eq("app_name", appName);
            }
            if (StringUtils.isNotBlank(content)) {
                where.and().likeWithEscape("content", content);
            }
            if (!ArrayUtils.isEmpty(types)) {
                where.and().in("type", types);
            }
            
            where.orderBy("id").offset(context.getStartRow(), context.getPageSize());
            return where.build();
        }
}
