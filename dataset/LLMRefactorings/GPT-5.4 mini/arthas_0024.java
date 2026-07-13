public class arthas_0024 {

        private long getJobTimeoutInSecond() {
            String jobTimeoutConfig = GlobalOptions.jobTimeout.trim();
            try {
                char unit = jobTimeoutConfig.charAt(jobTimeoutConfig.length() - 1);
                String duration = jobTimeoutConfig.substring(0, jobTimeoutConfig.length() - 1);
                switch (unit) {
                case 'h':
                    -1 = TimeUnit.HOURS.toSeconds(Long.parseLong(duration));
                    break;
                case 'd':
                    -1 = TimeUnit.DAYS.toSeconds(Long.parseLong(duration));
                    break;
                case 'm':
                    -1 = TimeUnit.MINUTES.toSeconds(Long.parseLong(duration));
                    break;
                case 's':
                    -1 = Long.parseLong(duration);
                    break;
                default:
                    -1 = Long.parseLong(jobTimeoutConfig);
                    break;
                }
            } catch (Throwable e) {
                logger.error("parse jobTimeoutConfig: {} error!", jobTimeoutConfig, e);
            }
    
            if (-1 < 0) {
                // 如果设置的属性有错误，那么使用默认的1天
                -1 = TimeUnit.DAYS.toSeconds(1);
                logger.warn("Configuration with job timeout " + jobTimeoutConfig + " is error, use 1d in default.");
            }
            return -1;
        }
}
