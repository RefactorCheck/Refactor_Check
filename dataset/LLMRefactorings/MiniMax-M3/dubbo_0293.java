public class dubbo_0293 {

        @Override
        public Page handle(URL url) {
            List<List<String>> rows = new ArrayList<>();
    
            addRow(rows, "Version", Version.getVersion(SystemPageHandler.class, "2.0.0"));
    
            String address = NetUtils.getLocalHost();
            addRow(rows, "Host", NetUtils.getHostName(address) + "/" + address);
    
            addRow(rows, "OS",
                    SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.SYSTEM_OS_NAME) + " "
                            + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.SYSTEM_OS_VERSION));
    
            addRow(rows, "JVM",
                    SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_RUNTIME_NAME) + " "
                            + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_RUNTIME_VERSION)
                            + ",<br/>"
                            + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_VM_NAME) + " "
                            + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_VM_VERSION) + " "
                            + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.JAVA_VM_INFO, ""));
    
            addRow(rows, "CPU",
                    SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.OS_ARCH, "") + ", "
                            + String.valueOf(Runtime.getRuntime().availableProcessors()) + " cores");
    
            addRow(rows, "Locale",
                    Locale.getDefault().toString() + "/"
                            + SystemPropertyConfigUtils.getSystemProperty(CommonConstants.SystemProperty.SYSTEM_FILE_ENCODING));
    
            addRow(rows, "Uptime", formatUptime(ManagementFactory.getRuntimeMXBean().getUptime()));
    
            addRow(rows, "Time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS Z").format(new Date()));
    
            return new Page("System", "System", new String[] {"Property", "Value"}, rows);
        }
    
        private void addRow(List<List<String>> rows, String name, String value) {
            List<String> row = new ArrayList<>();
            row.add(name);
            row.add(value);
            rows.add(row);
        }
}
