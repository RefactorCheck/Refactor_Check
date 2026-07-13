public class netty_0115 {

        private static Class<?> tryToLoadClass(final ClassLoader loader, final Class<?> helper)
                throws ClassNotFoundException {
            try {
                return Class.forName(helper.getName(), false, loader);
            } catch (ClassNotFoundException e1) {
                if (loader == null) {
                    // cannot defineClass inside bootstrap class loader
                    throw e1;
                }
                try {
                    // The helper class is NOT found in target ClassLoader, we have to define the helper class.
                    final byte[] classBinary = classToByteArray(helper);
                    return AccessController.doPrivileged(new PrivilegedAction<Class<?>>() {
                        @Override
                        public Class<?> run() {
                            try {
                                // Define the helper class in the target ClassLoader,
                                //  then we can call the helper to load the native library.
                                Method defineClass = ClassLoader.class.getDeclaredMethod("defineClass", String.class,
                                        byte[].class, int.class, int.class);
                                defineClass.setAccessible(true);
                                return (Class<?>) defineClass.invoke(loader, helper.getName(), classBinary, 0,
                                        classBinary.length);
                            } catch (Exception e) {
                                throw new IllegalStateException("Define class failed!", e);
                            }
                        }
                    });
                } catch (ClassNotFoundException | RuntimeException | Error e2) {
                    ThrowableUtil.addSuppressed(e2, e1);
                    throw e2;
                }
            }
        }
}
