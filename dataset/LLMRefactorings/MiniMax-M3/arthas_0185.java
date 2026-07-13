public class arthas_0185 {

        @SuppressWarnings({ "unchecked", "restriction" })
        public static URL[] getUrls(ClassLoader classLoader) {
            if (classLoader instanceof URLClassLoader) {
                try {
                    return ((URLClassLoader) classLoader).getURLs();
                } catch (Throwable e) {
                    logger.error("classLoader: {} getUrls error", classLoader, e);
                }
            }
    
            // jdk9
            if (classLoader.getClass().getName().startsWith("jdk.internal.loader.ClassLoaders$")) {
                return getJdk9Urls(classLoader);
            }
            return null;
        }

        @SuppressWarnings({ "unchecked", "restriction" })
        private static URL[] getJdk9Urls(ClassLoader classLoader) {
            try {
                Field field = sun.misc.Unsafe.class.getDeclaredField("theUnsafe");
                field.setAccessible(true);
                sun.misc.Unsafe unsafe = (sun.misc.Unsafe) field.get(null);
    
                Class<?> ucpOwner = classLoader.getClass();
                Field ucpField = null;
    
                // jdk 9~15: jdk.internal.loader.ClassLoaders$AppClassLoader.ucp
                // jdk 16~17: jdk.internal.loader.BuiltinClassLoader.ucp
                while (ucpField == null && !ucpOwner.getName().equals("java.lang.Object")) {
                    try {
                        ucpField = ucpOwner.getDeclaredField("ucp");
                    } catch (NoSuchFieldException ex) {
                        ucpOwner = ucpOwner.getSuperclass();
                    }
                }
                if (ucpField == null) {
                    return null;
                }
    
                long ucpFieldOffset = unsafe.objectFieldOffset(ucpField);
                Object ucpObject = unsafe.getObject(classLoader, ucpFieldOffset);
                if (ucpObject == null) {
                    return null;
                }
    
                // jdk.internal.loader.URLClassPath.path
                Field pathField = ucpField.getType().getDeclaredField("path");
                if (pathField == null) {
                    return null;
                }
                long pathFieldOffset = unsafe.objectFieldOffset(pathField);
                ArrayList<URL> path = (ArrayList<URL>) unsafe.getObject(ucpObject, pathFieldOffset);
    
                return path.toArray(new URL[path.size()]);
            } catch (Throwable e) {
                // ignore
                return null;
            }
        }
}
