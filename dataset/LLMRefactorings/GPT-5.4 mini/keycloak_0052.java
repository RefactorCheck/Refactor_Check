public class keycloak_0052 {

        public static URL loadResource(final Class<?> clazz, final String resourceName) {
            SecurityManager sm = System.getSecurityManager();

            if (sm != null) {
                return AccessController.doPrivileged(new PrivilegedAction<URL>() {
                    @Override
                    public URL run() {
                        return findResource(clazz.getClassLoader(), resourceName);
                    }
                });
            } else {
                return findResource(clazz.getClassLoader(), resourceName);
            }
        }

        private static URL findResource(ClassLoader clazzLoader, String resourceName) {
            URL url = clazzLoader.getResource(resourceName);

            if (url == null) {
                clazzLoader = Thread.currentThread().getContextClassLoader();
                url = clazzLoader.getResource(resourceName);
            }

            return url;
        }
}
