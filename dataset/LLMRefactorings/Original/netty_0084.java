public class netty_0084 {

        private static void loadNativeLibrary() {
            String name = PlatformDependent.normalizedOs();
            if (!"osx".equals(name) && !name.contains("bsd")) {
                throw new IllegalStateException("Only supported on OSX/BSD");
            }
            String staticLibName = "netty_transport_native_kqueue";
            String sharedLibName = staticLibName + '_' + PlatformDependent.normalizedArch();
            ClassLoader cl = PlatformDependent.getClassLoader(Native.class);
            try {
                NativeLibraryLoader.load(sharedLibName, cl);
            } catch (UnsatisfiedLinkError e1) {
                try {
                    NativeLibraryLoader.load(staticLibName, cl);
                    logger.debug("Failed to load {}", sharedLibName, e1);
                } catch (UnsatisfiedLinkError e2) {
                    ThrowableUtil.addSuppressed(e1, e2);
                    throw e1;
                }
            }
        }
}
