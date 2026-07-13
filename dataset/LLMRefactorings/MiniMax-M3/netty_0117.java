public class netty_0117 {

    private static int bitMode0() {
            int bitMode = getPositiveSystemProperty("io.netty.bitMode");
            if (bitMode > 0) {
                return bitMode;
            }
    
            bitMode = getPositiveSystemProperty("sun.arch.data.model");
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {} (sun.arch.data.model)", bitMode);
                return bitMode;
            }
            bitMode = getPositiveSystemProperty("com.ibm.vm.bitmode");
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", bitMode);
                return bitMode;
            }
    
            String arch = SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim();
            if ("amd64".equals(arch) || "x86_64".equals(arch)) {
                bitMode = 64;
            } else if ("i386".equals(arch) || "i486".equals(arch) || "i586".equals(arch) || "i686".equals(arch)) {
                bitMode = 32;
            }
    
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {} (os.arch: {})", bitMode, arch);
            }
    
            String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
            Pattern bitPattern = Pattern.compile("([1-9][0-9]+)-?bit");
            Matcher m = bitPattern.matcher(vm);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            } else {
                return 64;
            }
        }

        private static int getPositiveSystemProperty(String name) {
            int bitMode = SystemPropertyUtil.getInt(name, 0);
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {}", bitMode);
            }
            return bitMode;
        }
}
