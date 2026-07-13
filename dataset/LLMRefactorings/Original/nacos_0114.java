public class nacos_0114 {

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
            
            return helper.fetchPage(sqlCountRows + where, sqlFetchRows + where, params.toArray(),
                pageNo, pageSize,
                ROLE_INFO_ROW_MAPPER);
            
        }
}
