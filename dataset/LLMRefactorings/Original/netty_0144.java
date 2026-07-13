public class netty_0144 {

        private static void loadLibrary(final ClassLoader loader, final String name, final boolean absolute) {
            Throwable suppressed = null;
            try {
                try {
                    // Make sure the helper belongs to the target ClassLoader.
                    final Class<?> newHelper = tryToLoadClass(loader, NativeLibraryUtil.class);
                    loadLibraryByHelper(newHelper, name, absolute);
                    logger.debug("Successfully loaded the library {}", name);
                    return;
                } catch (UnsatisfiedLinkError e) { // Should by pass the UnsatisfiedLinkError here!
                    suppressed = e;
                } catch (Exception e) {
                    suppressed = e;
                }
                NativeLibraryUtil.loadLibrary(name, absolute);  // Fallback to local helper class.
                logger.debug("Successfully loaded the library {}", name);
            } catch (NoSuchMethodError nsme) {
                if (suppressed != null) {
                    ThrowableUtil.addSuppressed(nsme, suppressed);
                }
                throw new LinkageError(
                        "Possible multiple incompatible native libraries on the classpath for '" + name + "'?", nsme);
            } catch (UnsatisfiedLinkError ule) {
                if (suppressed != null) {
                    ThrowableUtil.addSuppressed(ule, suppressed);
                }
                throw ule;
            }
        }
}
