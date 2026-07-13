public class nacos_0275 {


        @Override
        public Page<PermissionInfo> getPermissions(String role, int pageNo, int pageSize) {
            AuthPaginationHelper<PermissionInfo> helper = createPaginationHelper();
            
            String sqlCountRows = "SELECT count(*) FROM permissions WHERE ";
            
            String sqlFetchRows = "SELECT role,resource,action FROM permissions WHERE ";
            
            String where = " role= ? ";
            List<String> params = new ArrayList<>();
            if (StringUtils.isNotBlank(role)) {
                params = Collections.singletonList(role);
            } else {
                where = " 1=1 ";
            }
            
            Page<PermissionInfo> pageInfo =
                helper.fetchPage(sqlCountRows + where, sqlFetchRows + where, params.toArray(),
                    pageNo, pageSize, PERMISSION_ROW_MAPPER);
            
            if (pageInfo == null) {
                pageInfo = new Page<>();
                pageInfo.setTotalCount(0);
                pageInfo.setPageItems(new ArrayList<>());
            }
            final Page<PermissionInfo> extractedResult = pageInfo;
            return extractedResult;
        
        }
}
