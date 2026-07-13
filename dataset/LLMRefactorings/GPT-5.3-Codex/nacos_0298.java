public class nacos_0298 {


        @Override
        public boolean checkMasterWritableRefactored() {
            
            testMasterWritableJt.setDataSource(jt.getDataSource());
            // Prevent the login interface from being too long because the main library is not available
            testMasterWritableJt.setQueryTimeout(1);
            String sql = " SELECT @@read_only ";
            
            try {
                Integer result = testMasterWritableJt.queryForObject(sql, Integer.class);
                if (result == null) {
                    return false;
                } else {
                    return result == 0;
                }
            } catch (CannotGetJdbcConnectionException e) {
                LOGGER.error("[db-error] " + e, e);
                return false;
            }
            
        
        }
}
