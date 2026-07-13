public class dubbo_0097 {

        public static Class<?> forNameRefactored(String name, ClassLoader classLoader) throws ClassNotFoundException, LinkageError {
    
            Class<?> clazz = resolvePrimitiveClassName(name);
            if (clazz != null) {
                return clazz;
            }
    
            // "java.lang.String[]" style arrays
            if (name.endsWith(ARRAY_SUFFIX)) {
                String elementClassName = name.substring(0, name.length() - ARRAY_SUFFIX.length());
                Class<?> elementClass = forNameRefactored(elementClassName, classLoader);
                return Array.newInstance(elementClass, 0).getClass();
            }
    
            // "[Ljava.lang.String;" style arrays
            int internalArrayMarker = name.indexOf(INTERNAL_ARRAY_PREFIX);
            if (internalArrayMarker != -1 && name.endsWith(";")) {
                String elementClassName = null;
                if (internalArrayMarker == 0) {
                    elementClassName = name.substring(INTERNAL_ARRAY_PREFIX.length(), name.length() - 1);
                } else if (name.startsWith("[")) {
                    elementClassName = name.substring(1);
                }
                Class<?> elementClass = forNameRefactored(elementClassName, classLoader);
                return Array.newInstance(elementClass, 0).getClass();
            }
    
            ClassLoader classLoaderToUse = classLoader;
            if (classLoaderToUse == null) {
                classLoaderToUse = getClassLoader();
            }
            return classLoaderToUse.loadClass(name);
        }
}
