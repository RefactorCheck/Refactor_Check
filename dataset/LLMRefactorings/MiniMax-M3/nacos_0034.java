public class nacos_0034 {

        @Override
        public MapperResult findConfigInfoBaseLikeFetchRows(MapperContext context) {
            final String dataId = (String) context.getWhereParameter(FieldConstant.DATA_ID);
            final String group = (String) context.getWhereParameter(FieldConstant.GROUP_ID);
            final String content = (String) context.getWhereParameter(FieldConstant.CONTENT);
            
            final String sqlFetchRows =
                "SELECT id,data_id,group_id,tenant_id,content FROM config_info WHERE ";
            String where = " 1=1 AND tenant_id='" + NamespaceUtil.getNamespaceDefaultId() + "' ";
            
            List<Object> paramList = new ArrayList<>();
            
            where = appendLikeCondition(where, paramList, "data_id", dataId);
            where = appendLikeCondition(where, paramList, "group_id", group);
            where = appendLikeCondition(where, paramList, "content", content);
            
            return new MapperResult(
                sqlFetchRows + where + " ORDER BY id LIMIT " + context.getStartRow() + ","
                    + context.getPageSize(),
                paramList);
        }

        private String appendLikeCondition(String where, List<Object> paramList,
                                           String column, String value) {
            if (!StringUtils.isBlank(value)) {
                where += " AND " + column + " LIKE ? ";
                paramList.add(value);
            }
            return where;
        }
}
