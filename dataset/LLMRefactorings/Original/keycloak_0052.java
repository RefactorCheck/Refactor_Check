public class keycloak_0052 {

        public static URL loadResource(final Class<?> clazz, final String resourceName) {
            SecurityManager sm = System.getSecurityManager();
    
            if (sm != null) {
                return AccessController.doPrivileged(new PrivilegedAction<URL>() {
                    @Override
                    public URL run() {
                        URL url;
                        ClassLoader clazzLoader = clazz.getClassLoader();
                        url = clazzLoader.getResource(resourceName);
    
                        if (url == null) {
                            clazzLoader = Thread.currentThread().getContextClassLoader();
                            url = clazzLoader.getResource(resourceName);
                        }
    
                        return url;
                    }
                });
            } else {
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
}
