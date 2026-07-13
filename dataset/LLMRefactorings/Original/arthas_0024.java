public class arthas_0024 {

        private long getJobTimeoutInSecond() {
            long result = -1;
            String jobTimeoutConfig = GlobalOptions.jobTimeout.trim();
            try {
                char unit = jobTimeoutConfig.charAt(jobTimeoutConfig.length() - 1);
                String duration = jobTimeoutConfig.substring(0, jobTimeoutConfig.length() - 1);
                switch (unit) {
                case 'h':
                    result = TimeUnit.HOURS.toSeconds(Long.parseLong(duration));
                    break;
                case 'd':
                    result = TimeUnit.DAYS.toSeconds(Long.parseLong(duration));
                    break;
                case 'm':
                    result = TimeUnit.MINUTES.toSeconds(Long.parseLong(duration));
                    break;
                case 's':
                    result = Long.parseLong(duration);
                    break;
                default:
                    result = Long.parseLong(jobTimeoutConfig);
                    break;
                }
            } catch (Throwable e) {
                logger.error("parse jobTimeoutConfig: {} error!", jobTimeoutConfig, e);
            }
    
            if (result < 0) {
                // 如果设置的属性有错误，那么使用默认的1天
                result = TimeUnit.DAYS.toSeconds(1);
                logger.warn("Configuration with job timeout " + jobTimeoutConfig + " is error, use 1d in default.");
            }
            return result;
        }
}
