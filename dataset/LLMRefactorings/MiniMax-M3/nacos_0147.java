public class nacos_0147 {

    private static final String COUNT_CONFIG_INFO_SQL = "SELECT count(*) FROM config_info a LEFT JOIN config_tags_relation b ON a.id=b.id";

        default MapperResult findConfigInfoLike4PageCountRows(final MapperContext context) {
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);
            final String[] types = (String[]) context.getWhereParameter(FieldConstant.TYPE);
            
            WhereBuilder where = new WhereBuilder(COUNT_CONFIG_INFO_SQL);
            
            where.like("a.tenant_id", tenantId);
            if (StringUtils.isNotBlank(dataId)) {
                where.and().like("a.data_id", dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                where.and().like("a.group_id", group);
            }
            if (StringUtils.isNotBlank(appName)) {
                where.and().eq("a.app_name", appName);
            }
            if (StringUtils.isNotBlank(content)) {
                where.and().like("a.content", content);
            }
            if (!ArrayUtils.isEmpty(tagArr)) {
                where.and().startParentheses();
                for (int i = 0; i < tagArr.length; i++) {
                    if (i != 0) {
                        where.or();
                    }
                    where.like("b.tag_name", tagArr[i]);
                }
                where.endParentheses();
            }
            if (!ArrayUtils.isEmpty(types)) {
                where.and().in("a.type", types);
            }
            
            return where.build();
        }
}
