public class nacos_0051 {

        @Override
        public Page<RoleInfo> getRolesByUserNameAndRoleName(String username, String role, int pageNo,
            int pageSize) {
            
            AuthPaginationHelper<RoleInfo> helper = createPaginationHelper();
            
            String sqlCountRows = "SELECT count(*) FROM roles ";
            
            String sqlFetchRows = "SELECT role,username FROM roles ";
            
            StringBuilder where = new StringBuilder(" WHERE 1 = 1 ");
            List<String> params = new ArrayList<>();
            if (StringUtils.isNotBlank(username)) {
                where.append(" AND username = ? ");
                params.add(username);
            }
            if (StringUtils.isNotBlank(role)) {
                where.append(" AND role = ? ");
                params.add(role);
            }
            
            try {
                return helper.fetchPage(sqlCountRows + where, sqlFetchRows + where, params.toArray(),
                    pageNo, pageSize,
                    ROLE_INFO_ROW_MAPPER);
            } catch (CannotGetJdbcConnectionException e) {
                LOGGER.error("[db-error] " + e.toString(), e);
                throw e;
            }
        }
}
