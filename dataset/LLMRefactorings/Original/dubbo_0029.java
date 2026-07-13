public class dubbo_0029 {

        @SuppressWarnings("deprecation")
        public static int getServerShutdownTimeout(ScopeModel scopeModel) {
            if (expectedShutdownTime < System.currentTimeMillis()) {
                return 1;
            }
            int timeout = DEFAULT_SERVER_SHUTDOWN_TIMEOUT;
            Configuration configuration = getGlobalConfiguration(scopeModel);
            String value = StringUtils.trim(configuration.getString(SHUTDOWN_WAIT_KEY));
    
            if (StringUtils.isNotEmpty(value)) {
                try {
                    timeout = Integer.parseInt(value);
                } catch (Exception e) {
                    // ignore
                }
            } else {
                value = StringUtils.trim(configuration.getString(SHUTDOWN_WAIT_SECONDS_KEY));
                if (StringUtils.isNotEmpty(value)) {
                    try {
                        timeout = Integer.parseInt(value) * 1000;
                    } catch (Exception e) {
                        // ignore
                    }
                }
            }
    
            if (expectedShutdownTime - System.currentTimeMillis() < timeout) {
                return (int) Math.max(1, expectedShutdownTime - System.currentTimeMillis());
            }
    
            return timeout;
        }
}
