public class keycloak_0052 {

    public static URL loadResource(final Class<?> clazz, final String resourceName) {
        SecurityManager sm = System.getSecurityManager();

        if (sm != null) {
            return AccessController.doPrivileged(new PrivilegedAction<URL>() {
                @Override
                public URL run() {
                    return loadResourceFromClassLoader(clazz, resourceName);
                }
            });
        } else {
            return loadResourceFromClassLoader(clazz, resourceName);
        }
    }

    private static URL loadResourceFromClassLoader(final Class<?> clazz, final String resourceName) {
        URL url;
        ClassLoader clazzLoader = clazz.getClassLoader();
        url = clazzLoader.getResource(resourceName);

        if (url == null) {
            clazzLoader = Thread.currentThread().getContextClassLoader();
            url = clazzLoader.getResource(resourceName);
        }

        return url;
    }
}
