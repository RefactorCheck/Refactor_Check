public class dubbo_0293 {

        @Override
        public Page handle(URL url) {
            List<List<String>> rows = new ArrayList<>();
            List<String> row;
    
            row = new ArrayList<>();
            row.add("Version");
            row.add(Version.getVersion(SystemPageHandler.class, "2.0.0"));
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("Host");
            String address = NetUtils.getLocalHost();
            row.add(NetUtils.getHostName(address) + "/" + address);
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("OS");
            row.add(SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.SYSTEM_OS_NAME) + " "
                    + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.SYSTEM_OS_VERSION));
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("JVM");
            row.add(SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_RUNTIME_NAME) + " "
                    + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_RUNTIME_VERSION)
                    + ",<br/>"
                    + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_VM_NAME) + " "
                    + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_VM_VERSION) + " "
                    + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_VM_INFO, ""));
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("CPU");
            row.add(SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.OS_ARCH, "") + ", "
                    + String.valueOf(Runtime.getRuntime().availableProcessors()) + " cores");
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("Locale");
            row.add(Locale.getDefault().toString() + "/"
                    + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.SYSTEM_FILE_ENCODING));
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("Uptime");
            row.add(formatUptime(ManagementFactory.getRuntimeMXBean().getUptime()));
            rows.add(row);
    
            row = new ArrayList<>();
            row.add("Time");
            row.add(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(new Date()));
            rows.add(row);
    
            return new Page("System", "System", new String[] {"Property", "Value"}, rows);
        }
}
