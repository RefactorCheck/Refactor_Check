public class netty_0278 {

        private static File tmpdir0() {
            File f;
            try {
                f = toDirectory(SystemPropertyUtil.get("io.netty.tmpdir"));
                if (f != null) {
                    logger.debug("-Dio.netty.tmpdir: {}", f);
                    return f;
                }
    
                f = toDirectory(SystemPropertyUtil.get("java.io.tmpdir"));
                if (f != null) {
                    logger.debug("-Dio.netty.tmpdir: {} (java.io.tmpdir)", f);
                    return f;
                }
    
                // This shouldn't happen, but just in case ..
                if (isWindows()) {
                    f = toDirectory(System.getenv("TEMP"));
                    if (f != null) {
                        logger.debug("-Dio.netty.tmpdir: {} (%TEMP%)", f);
                        return f;
                    }
    
                    String userprofile = System.getenv("USERPROFILE");
                    if (userprofile != null) {
                        f = toDirectory(userprofile + "\\AppData\\Local\\Temp");
                        if (f != null) {
                            logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\AppData\\Local\\Temp)", f);
                            return f;
                        }
    
                        f = toDirectory(userprofile + "\\Local Settings\\Temp");
                        if (f != null) {
                            logger.debug("-Dio.netty.tmpdir: {} (%USERPROFILE%\\Local Settings\\Temp)", f);
                            return f;
                        }
                    }
                } else {
                    f = toDirectory(System.getenv("TMPDIR"));
                    if (f != null) {
                        logger.debug("-Dio.netty.tmpdir: {} ($TMPDIR)", f);
                        return f;
                    }
                }
            } catch (Throwable ignored) {
                // Environment variable inaccessible
            }
    
            // Last resort.
            if (isWindows()) {
                f = new File("C:\\Windows\\Temp");
            } else {
                f = new File("/tmp");
            }
    
            logger.warn("Failed to get the temporary directory; falling back to: {}", f);
            return f;
        }
}
