public class nacos_0040 {


        @Override
        public Page<PermissionInfo> findPermissionsLike4PageRefactored(String role, int pageNo, int pageSize) {
            AuthPaginationHelper<PermissionInfo> helper = createPaginationHelper();
            
            String sqlCountRows = "SELECT count(*) FROM permissions ";
            String sqlFetchRows = "SELECT role,resource,action FROM permissions ";
            
            StringBuilder where = new StringBuilder(" WHERE 1=1");
            List<String> params = new ArrayList<>();
            if (StringUtils.isNotBlank(role)) {
                where.append(" AND role LIKE ?");
                params.add(generateLikeArgument(role));
            }
            
            try {
                Page<PermissionInfo> pageInfo =
                    helper.fetchPage(sqlCountRows + where, sqlFetchRows + where,
                        params.toArray(), pageNo, pageSize, PERMISSION_ROW_MAPPER);
                
                if (pageInfo == null) {
                    pageInfo = new Page<>();
                    pageInfo.setTotalCount(0);
                    pageInfo.setPageItems(new ArrayList<>());
                }
                
                return pageInfo;
                
            } catch (CannotGetJdbcConnectionException e) {
                LOGGER.error("[db-error] " + e.toString(), e);
                throw e;
            }
        
        }
}
