public class nacos_0034 {

        @Override
        public MapperResult findConfigInfoBaseLikeFetchRowsRefactored(MapperContext context) {
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            
            final String sqlFetchRows =
                "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
            String where = " 1=1 AND tenant_id='" + NamespaceUtil.getNamespaceDefaultId() + "' ";
            
            List<Object> paramList = new ArrayList<>();
            
            if (!StringUtils.isBlank(dataId)) {
                where += " AND data_id LIKE ? ";
                paramList.add(dataId);
            }
            if (!StringUtils.isBlank(group)) {
                where += " AND group_id LIKE ? ";
                paramList.add(group);
            }
            if (!StringUtils.isBlank(content)) {
                where += " AND content LIKE ? ";
                paramList.add(content);
            }
            return new MapperResult(
                sqlFetchRows + where + " ORDER BY id LIMIT " + context.getStartRow() + ","
                    + context.getPageSize(),
                paramList);
        }
}
