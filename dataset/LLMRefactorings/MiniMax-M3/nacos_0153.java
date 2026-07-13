public class nacos_0153 {

    @Override
    public MapperResult findConfigInfo4PageFetchRows(MapperContext context) {
        final String tenant = (String) context.getWhereParameter(FieldConstant.TENANT_ID);
        final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
        final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
        final String appName = (String) context.getWhereParameter(FieldConstant.APP_NAME);
        final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
        
        List<Object> paramList = new ArrayList<>();
        StringBuilder idSql = new StringBuilder(
            "SELECT id FROM config_info WHERE tenant_id=? ");
        paramList.add(tenant);
        
        appendWhereConditions(idSql, paramList, dataId, group, appName, content);
        
        idSql.append(" ORDER BY id OFFSET ")
            .append(context.getStartRow())
            .append(" ROWS FETCH NEXT ")
            .append(context.getPageSize())
            .append(" ROWS ONLY");
        
        String sql =
            "WITH tag_agg AS ( "
                + "   SELECT id, LISTAGG(tag_name, ',') "
                + "   WITHIN GROUP (ORDER BY tag_name) AS config_tags "
                + "   FROM config_tags_relation GROUP BY id "
                + ") "
                + "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,"
                + "       a.content,a.md5,a.type,a.encrypted_data_key,a.c_desc,"
                + "       t.config_tags "
                + "FROM config_info a "
                + "JOIN ("
                + idSql.toString()
                + ") x ON a.id = x.id "
                + "LEFT JOIN tag_agg t ON a.id = t.id";
        
        return new MapperResult(sql, paramList);
    }
    
    private void appendWhereConditions(StringBuilder idSql, List<Object> paramList,
                                       String dataId, String group, String appName, String content) {
        if (StringUtils.isNotBlank(dataId)) {
            idSql.append(" AND data_id=?");
            paramList.add(dataId);
        }
        if (StringUtils.isNotBlank(group)) {
            idSql.append(" AND group_id=?");
            paramList.add(group);
        }
        if (StringUtils.isNotBlank(appName)) {
            idSql.append(" AND app_name=?");
            paramList.add(appName);
        }
        if (!StringUtils.isBlank(content)) {
            idSql.append(" AND content LIKE ?");
            paramList.add(content);
        }
    }
}
