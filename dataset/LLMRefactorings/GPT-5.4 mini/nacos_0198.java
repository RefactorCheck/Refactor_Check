public class nacos_0198 {

        @Override
        public MapperResult findConfigInfoLike4PageFetchRowsRefactored(MapperContext context) {
            final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);
            final String[] types = (String[]) context.getWhereParameter(FieldConstant.TYPE);
            
            // 构建内层查询：根据标签条件筛选配置
            WhereBuilder innerWhere = new WhereBuilder(
                "SELECT DISTINCT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content,a.md5,a.encrypted_data_key,a.type,a.c_desc "
                    + "FROM config_info a LEFT JOIN config_tags_relation b ON a.id=b.id");
            
            innerWhere.like("a.tenant_id", tenant);
            
            if (StringUtils.isNotBlank(dataId)) {
                innerWhere.and().like("a.data_id", dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                innerWhere.and().like("a.group_id", group);
            }
            if (StringUtils.isNotBlank(appName)) {
                innerWhere.and().eq("a.app_name", appName);
            }
            if (StringUtils.isNotBlank(content)) {
                innerWhere.and().like("a.content", content);
            }
            if (!ArrayUtils.isEmpty(tagArr)) {
                innerWhere.and().startParentheses();
                for (int i = 0; i < tagArr.length; i++) {
                    if (i != 0) {
                        innerWhere.or();
                    }
                    innerWhere.like("b.tag_name", tagArr[i]);
                }
                innerWhere.endParentheses();
            }
            if (!ArrayUtils.isEmpty(types)) {
                innerWhere.and().in("a.type", types);
            }
            
            innerWhere.orderBy("a.id").limit(context.getStartRow(), context.getPageSize());
            MapperResult innerResult = innerWhere.build();
            
            // 构建外层查询：获取筛选出的配置的完整标签信息
            final String sql =
                "SELECT c.id,c.data_id,c.group_id,c.tenant_id,c.app_name,c.content,c.md5,c.encrypted_data_key,c.type,c.c_desc,"
                    + "GROUP_CONCAT(DISTINCT d.tag_name SEPARATOR ',') as config_tags "
                    + "FROM (" + innerResult.getSql() + ") c "
                    + "LEFT JOIN config_tags_relation d ON c.id=d.id "
                    + "GROUP BY c.id,c.data_id,c.group_id,c.tenant_id,c.app_name,c.content,c.md5,c.encrypted_data_key,c.type,c.c_desc";
            
            return new MapperResult(sql, innerResult.getParamList());
        }
}
