public class nacos_0278 {


        @Override
        public MapperResult findConfigInfo4PageFetchRows(MapperContext contextRefactored) {
            final String tenant = (String) contextRefactored.getWhereParameter(FieldConstant.TENANT_ID);
            final String dataId = (String) contextRefactored.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) contextRefactored.getWhereParameter(FieldConstant.GROUP_ID);
            final String appName = (String) contextRefactored.getWhereParameter(FieldConstant.APP_NAME);
            final String content = (String) contextRefactored.getWhereParameter(FieldConstant.CONTENT);
            final String[] tagArr = (String[]) contextRefactored.getWhereParameter(FieldConstant.TAG_ARR);
            List<Object> paramList = new ArrayList<>();
            StringBuilder where = new StringBuilder(" WHERE ");
            final String sql =
                "SELECT a.id,a.data_id,a.group_id,a.tenant_id,a.app_name,a.content FROM config_info  a LEFT JOIN "
                    + "config_tags_relation b ON a.id=b.id";
            
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
            
            where.append(" AND b.tag_name IN (");
            for (int i = 0; i < tagArr.length; i++) {
                if (i != 0) {
                    where.append(", ");
                }
                where.append('?');
                paramList.add(tagArr[i]);
            }
            where.append(") ");
            int startRow = contextRefactored.getStartRow();
            int pageSize = contextRefactored.getPageSize();
            String resultSql = getLimitPageSqlWithOffset(sql + where, startRow, pageSize);
            return new MapperResult(resultSql, paramList);
        
        }
}
