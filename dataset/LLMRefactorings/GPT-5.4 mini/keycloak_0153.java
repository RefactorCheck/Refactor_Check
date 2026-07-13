public class keycloak_0153 {

        public static SystemInfoRepresentation create(long serverStartupTime, String serverVersion) {
            SystemInfoRepresentation rep = new SystemInfoRepresentation();
            rep.version = serverVersion;
            rep.serverTime = new Date().toString();
            rep.uptimeMillis = System.currentTimeMillis() - serverStartupTime;
            rep.uptime = formatUptime(rep.uptimeMillis);
            rep.javaVersion = System.getProperty("java.version");
            rep.javaVendor = System.getProperty("java.vendor");
            rep.javaVm = System.getProperty("java.vm.name");
            rep.javaVmVersion = System.getProperty("java.vm.version");
            rep.javaRuntime = System.getProperty("java.runtime.name");
            rep.javaHome = System.getProperty("java.home");
            rep.osName = System.getProperty("os.name");
            rep.osArchitecture = System.getProperty("os.arch");
            rep.osVersion = System.getProperty("os.version");
            rep.fileEncoding = System.getProperty("file.encoding");
            rep.userName = System.getProperty("user.name");
            rep.userDir = System.getProperty("user.dir");
            rep.userTimezone = System.getProperty("user.timezone");
            if (System.getProperty("user.country") != null && System.getProperty("user.language") != null) {
                Locale userLocale = new Locale(System.getProperty("user.language"), System.getProperty("user.country"));
                rep.userLocale = userLocale.toString();
            }
            return rep;
        }
}
