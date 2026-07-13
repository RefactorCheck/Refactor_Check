public class nacos_0078 {

    @Override
    public Page<RoleInfo> getRoles(int pageNo, int pageSize) {
        AuthPaginationHelper<RoleInfo> helper = createPaginationHelper();

        String sqlCountRows = "SELECT count(*) FROM (SELECT DISTINCT role FROM roles) roles WHERE ";

        String sqlFetchRows = "SELECT role,username FROM roles WHERE ";

        String where = " 1=1 ";

        try {
            Page<RoleInfo> pageInfo = helper.fetchPage(sqlCountRows + where, sqlFetchRows + where,
                    new ArrayList<String>().toArray(), pageNo, pageSize, ROLE_INFO_ROW_MAPPER);
            return handleNullPageInfo(pageInfo);
        } catch (CannotGetJdbcConnectionException e) {
            LOGGER.error("[db-error] " + e.toString(), e);
            throw e;
        }
    }

    private Page<RoleInfo> handleNullPageInfo(Page<RoleInfo> pageInfo) {
        if (pageInfo == null) {
            pageInfo = new Page<>();
            pageInfo.setTotalCount(0);
            pageInfo.setPageItems(new ArrayList<>());
        }
        return pageInfo;
    }
}
