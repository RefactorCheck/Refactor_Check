public class nacos_0145 {

        @Override
        public Page<RoleInfo> getRoles(int pageNo, int pageSize) {
            
            AuthPaginationHelper<RoleInfo> helper = createPaginationHelper();
            
            String sqlCountRows = "SELECT count(*) FROM (SELECT DISTINCT role FROM roles) roles WHERE ";
            
            String sqlFetchRows = "SELECT role,username FROM roles WHERE ";
            
            String where = " 1=1 ";
            
            Page<RoleInfo> pageInfo = helper.fetchPage(sqlCountRows + where, sqlFetchRows + where,
                new ArrayList<String>().toArray(), pageNo, pageSize, ROLE_INFO_ROW_MAPPER);
            if (pageInfo == null) {
                pageInfo = createEmptyPage();
            }
            return pageInfo;
            
        }
        
        private Page<RoleInfo> createEmptyPage() {
            Page<RoleInfo> emptyPage = new Page<>();
            emptyPage.setTotalCount(0);
            emptyPage.setPageItems(new ArrayList<>());
            return emptyPage;
        }
}
