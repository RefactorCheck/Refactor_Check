public class netty_0084 {

        private static final String STATIC_LIB_NAME = "netty_transport_native_kqueue";

        private static void loadNativeLibrary() {
            String name = PlatformDependent.normalizedOs();
            if (!"osx".equals(name) && !name.contains("bsd")) {
                throw new IllegalStateException("Only supported on OSX/BSD");
            }
            String sharedLibName = STATIC_LIB_NAME + '_' + PlatformDependent.normalizedArch();
            ClassLoader cl = PlatformDependent.getClassLoader(Native.class);
            try {
                NativeLibraryLoader.load(sharedLibName, cl);
            } catch (UnsatisfiedLinkError e1) {
                try {
                    NativeLibraryLoader.load(STATIC_LIB_NAME, cl);
                    logger.debug("Failed to load {}", sharedLibName, e1);
                } catch (UnsatisfiedLinkError e2) {
                    ThrowableUtil.addSuppressed(e1, e2);
                    throw e1;
                }
            }
        }
}
