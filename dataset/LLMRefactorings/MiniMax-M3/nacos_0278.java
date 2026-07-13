public class nacos_0278 {

        @Override
        public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
            final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            final String[] tagArr = (String[]) context.getWhereParameter(FieldConstant.TAG_ARR);
            final String sql =
                "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content FROM config_info  a LEFT JOIN "
                    + "config_tags_relation b ON a.id=b.id";
            List<Object> paramList = new ArrayList<>();
            String where = buildWhereClause(tenant, dataId, group, appName, content, tagArr, paramList);
            int startRow = context.getStartRow();
            int pageSize = context.getPageSize();
            String resultSql = getLimitPageSqlWithOffset(sql + where, startRow, pageSize);
            return new MapperResult(resultSql, paramList);
        }

        private String buildWhereClause(String tenant, String dataId, String group, String appName,
                                        String content, String[] tagArr, List<Object> paramList) {
            StringBuilder where = new StringBuilder(" WHERE ");
            where.append(" a.tenant_id=? ");
            paramList.add(tenant);
            if (StringUtils.isNotBlank(dataId)) {
                where.append(" AND a.data_id=? ");
                paramList.add(dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                where.append(" AND a.group_id=? ");
                paramList.add(group);
            }
            if (StringUtils.isNotBlank(appName)) {
                where.append(" AND a.app_name=? ");
                paramList.add(appName);
            }
            if (!StringUtils.isBlank(content)) {
                where.append(" AND a.content LIKE ? ");
                paramList.add(content);
            }
            appendTagInClause(where, paramList, tagArr);
            return where.toString();
        }

        private void appendTagInClause(StringBuilder where, List<Object> paramList, String[] tagArr) {
            where.append(" AND b.tag_name IN (");
            for (int i = 0; i < tagArr.length; i++) {
                if (i != 0) {
                    where.append(", ");
                }
                where.append('?');
                paramList.add(tagArr[i]);
            }
            where.append(") ");
        }
}
