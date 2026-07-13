public class netty_0117 {

        private static int bitMode0() {
            // Check user-specified bit mode first.
            int bitMode = SystemPropertyUtil.getInt("io.netty.bitMode", 0);
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {}", bitMode);
                return bitMode;
            }
    
            // And then the vendor specific ones which is probably most reliable.
            bitMode = SystemPropertyUtil.getInt("sun.(SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()).data.model", 0);
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {} (sun.(SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()).data.model)", bitMode);
                return bitMode;
            }
            bitMode = SystemPropertyUtil.getInt("com.ibm.vm.bitmode", 0);
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {} (com.ibm.vm.bitmode)", bitMode);
                return bitMode;
            }
    
            // os.(SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()) also gives us a good hint.

            if ("amd64".equals((SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim())) || "x86_64".equals((SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()))) {
                bitMode = 64;
            } else if ("i386".equals((SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim())) || "i486".equals((SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim())) || "i586".equals((SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim())) || "i686".equals((SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()))) {
                bitMode = 32;
            }
    
            if (bitMode > 0) {
                logger.debug("-Dio.netty.bitMode: {} (os.(SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()): {})", bitMode, (SystemPropertyUtil.get("os.arch", "").toLowerCase(Locale.US).trim()));
            }
    
            // Last resort: guess from VM name and then fall back to most common 64-bit mode.
            String vm = SystemPropertyUtil.get("java.vm.name", "").toLowerCase(Locale.US);
            Pattern bitPattern = Pattern.compile("([1-9][0-9]+)-?bit");
            Matcher m = bitPattern.matcher(vm);
            if (m.find()) {
                return Integer.parseInt(m.group(1));
            } else {
                return 64;
            }
        }
}
