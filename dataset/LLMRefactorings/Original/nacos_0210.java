public class nacos_0210 {

        @Override
        public MapperResult findConfigInfoLike4PageCountRows(MapperContext context) {
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            final String tenantId = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);
            
            WhereBuilder where = new WhereBuilder(
                "SELECT count(*) FROM config_info a LEFT JOIN config_tags_relation b ON a.id=b.id");
            
            where.likeWithEscape("a.tenant_id", tenantId);
            if (StringUtils.isNotBlank(dataId)) {
                where.and().likeWithEscape("a.data_id", dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                where.and().likeWithEscape("a.group_id", group);
            }
            if (StringUtils.isNotBlank(appName)) {
                where.and().eq("a.app_name", appName);
            }
            if (StringUtils.isNotBlank(content)) {
                where.and().likeWithEscape("a.content", content);
            }
            if (!ArrayUtils.isEmpty(tagArr)) {
                where.and().startParentheses();
                for (int i = 0; i < tagArr.length; i++) {
                    if (i != 0) {
                        where.or();
                    }
                    where.likeWithEscape("b.tag_name", tagArr[i]);
                }
                where.endParentheses();
            }
            return where.build();
        }
}
