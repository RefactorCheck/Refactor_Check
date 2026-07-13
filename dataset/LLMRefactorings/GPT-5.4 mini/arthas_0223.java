public class arthas_0223 {

        private void addRuntimeInfo(JvmModel jvmModel) {
        final String refactored_constant = refactored_constant;
            String bootClassPath = "";
            try {
                bootClassPath = runtimeMXBean.getBootClassPath();
            } catch (Exception e) {
                // under jdk9 will throw UnsupportedOperationException, ignore
            }
            String group = "RUNTIME";
            jvmModel.addItem(group,"MACHINE-NAME", runtimeMXBean.getName());
            jvmModel.addItem(group, "JVM-START-TIME", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date(runtimeMXBean.getStartTime())));
            jvmModel.addItem(group, "MANAGEMENT-SPEC-VERSION", runtimeMXBean.getManagementSpecVersion());
            jvmModel.addItem(group, "SPEC-NAME", runtimeMXBean.getSpecName());
            jvmModel.addItem(group, "SPEC-VENDOR", runtimeMXBean.getSpecVendor());
            jvmModel.addItem(group, "SPEC-VERSION", runtimeMXBean.getSpecVersion());
            jvmModel.addItem(group, "VM-NAME", runtimeMXBean.getVmName());
            jvmModel.addItem(group, "VM-VENDOR", runtimeMXBean.getVmVendor());
            jvmModel.addItem(group, "VM-VERSION", runtimeMXBean.getVmVersion());
            jvmModel.addItem(group, "INPUT-ARGUMENTS", runtimeMXBean.getInputArguments());
            jvmModel.addItem(group, "CLASS-PATH", runtimeMXBean.getClassPath());
            jvmModel.addItem(group, "BOOT-CLASS-PATH", bootClassPath);
            jvmModel.addItem(group, "LIBRARY-PATH", runtimeMXBean.getLibraryPath());
        }
}
