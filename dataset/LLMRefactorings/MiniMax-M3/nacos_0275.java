public class nacos_0275 {

    private static final String SQL_COUNT_ROWS = "SELECT count(*) FROM permissions WHERE ";
    private static final String SQL_FETCH_ROWS = "SELECT role,resource,action FROM permissions WHERE ";

    @Override
    public Page<PermissionInfo> getPermissions(String role, int pageNo, int pageSize) {
        AuthPaginationHelper<PermissionInfo> helper = createPaginationHelper();
        
        String where = " role= ? ";
        List<String> params = new ArrayList<>();
        if (StringUtils.isNotBlank(role)) {
            params = Collections.singletonList(role);
        } else {
            where = " 1=1 ";
        }
        
        Page<PermissionInfo> pageInfo =
            helper.fetchPage(SQL_COUNT_ROWS + where, SQL_FETCH_ROWS + where, params.toArray(),
                pageNo, pageSize, PERMISSION_ROW_MAPPER);
        
        if (pageInfo == null) {
            pageInfo = new Page<>();
            pageInfo.setTotalCount(0);
            pageInfo.setPageItems(new ArrayList<>());
        }
        return pageInfo;
    }
}
