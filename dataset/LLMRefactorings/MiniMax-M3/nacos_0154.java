public class nacos_0154 {

        @Override
        public MapperResult findConfigInfoLike4PageFetchRows(MapperContext context) {
            final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            final String[] types = (String[]) context.getWhereParameter(FieldConstant.TYPE);
            
            List<Object> paramList = new ArrayList<>();
            
            // 性能优化：先 LIMIT 再 JOIN，减少 JOIN 和 GROUP BY 的数据量
            StringBuilder innerSql =
                new StringBuilder("SELECT id,data_id,group_id,tenant_id,app_name,content,md5,"
                    + "encrypted_data_key,type,c_desc,gmt_modified FROM config_info WHERE tenant_id LIKE ?");
            paramList.add(tenant);
            
            appendWhereConditions(innerSql, paramList, dataId, group, appName, content, types);
            
            // 先分页，减少后续 JOIN 的数据量
            innerSql.append(" ORDER BY id LIMIT ").append(context.getStartRow()).append(",")
                .append(context.getPageSize());
            
            // 外层查询：对分页后的结果进行标签关联
            final String sql =
                "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content,a.md5,"
                    + "a.encrypted_data_key,a.type,a.c_desc,a.gmt_modified,"
                    + "GROUP_CONCAT(b.tag_name SEPARATOR ',') as config_tags "
                    + "FROM (" + innerSql + ") a LEFT JOIN config_tags_relation b ON a.id=b.id "
                    + "GROUP BY a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content,a.md5,"
                    + "a.encrypted_data_key,a.type,a.c_desc,a.gmt_modified";
            
            return new MapperResult(sql, paramList);
        }

        private void appendWhereConditions(StringBuilder innerSql, List<Object> paramList,
                String dataId, String group, String appName, String content, String[] types) {
            if (StringUtils.isNotBlank(dataId)) {
                innerSql.append(" AND data_id LIKE ?");
                paramList.add(dataId);
            }
            if (StringUtils.isNotBlank(group)) {
                innerSql.append(" AND group_id LIKE ?");
                paramList.add(group);
            }
            if (StringUtils.isNotBlank(appName)) {
                innerSql.append(" AND app_name = ?");
                paramList.add(appName);
            }
            if (StringUtils.isNotBlank(content)) {
                innerSql.append(" AND content LIKE ?");
                paramList.add(content);
            }
            if (!ArrayUtils.isEmpty(types)) {
                innerSql.append(" AND type IN (");
                for (int i = 0; i < types.length; i++) {
                    if (i != 0) {
                        innerSql.append(", ");
                    }
                    innerSql.append("?");
                    paramList.add(types[i]);
                }
                innerSql.append(")");
            }
        }
}
