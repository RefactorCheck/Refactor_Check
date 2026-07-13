public class nacos_0298 {

        private static final String CHECK_READ_ONLY_SQL = " SELECT @@read_only ";

        @Override
        public boolean checkMasterWritable() {
            
            testMasterWritableJt.setDataSource(jt.getDataSource());
            // Prevent the login interface from being too long because the main library is not available
            testMasterWritableJt.setQueryTimeout(1);
            
            try {
                Integer result = testMasterWritableJt.queryForObject(CHECK_READ_ONLY_SQL, Integer.class);
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
