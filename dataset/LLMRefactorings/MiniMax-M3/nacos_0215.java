public class nacos_0215 {

        @Override
        public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
            List<Object> paramList = new ArrayList<>();
            String innerSql = buildInnerSql(context, paramList);

            final String sql =
                "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content,a.md5,a.type,a.encrypted_data_key,a.c_desc,"
                    + "GROUP_CONCAT(b.tag_name SEPARATOR ',') as config_tags "
                    + "FROM (" + innerSql + ") a LEFT JOIN config_tags_relation b ON a.id=b.id "
                    + "GROUP BY a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content,a.md5,a.type,a.encrypted_data_key,a.c_desc";

            return new MapperResult(sql, paramList);
        }

        private String buildInnerSql(MapperContext context, List<Object> paramList) {
            final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);

            StringBuilder innerSql = new StringBuilder("SELECT id,data_id,group_id,tenant_id,app_name,"
                + "content,md5,type,encrypted_data_key,c_desc FROM config_info WHERE tenant_id=?");
            paramList.add(tenant);

            if (StringUtils.isNotBlank(dataId)) {
                innerSql.append(" AND data_id=?");
                paramList.add(dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                innerSql.append(" AND group_id=?");
                paramList.add(group);
            }
            if (StringUtils.isNotBlank(appName)) {
                innerSql.append(" AND app_name=?");
                paramList.add(appName);
            }
            if (!StringUtils.isBlank(content)) {
                innerSql.append(" AND content LIKE ?");
                paramList.add(content);
            }

            innerSql.append(" ORDER BY id LIMIT ").append(context.getStartRow()).append(",")
                .append(context.getPageSize());

            return innerSql.toString();
        }
}
