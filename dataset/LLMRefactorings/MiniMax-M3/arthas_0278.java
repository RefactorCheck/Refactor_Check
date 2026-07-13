public class arthas_0278 {

        private static Class<?> helperClassNameWithClassLoader(ClassLoader classLoader, Class<?> helperClass) {
            String classLoaderHash = ClassLoaderUtils.classLoaderHash(classLoader);
            String className = helperClass.getName();
            // if want to debug, change to return className
            String helperClassName = className + arthasClassLoaderHash + classLoaderHash;
    
            try {
                return classLoader.loadClass(helperClassName);
            } catch (ClassNotFoundException e) {
                return defineHelperClass(helperClass, helperClassName, classLoader);
            }
        }

        private static Class<?> defineHelperClass(Class<?> helperClass, String helperClassName, ClassLoader classLoader) {
            try {
                byte[] helperClassBytes = AsmRenameUtil.renameClass(classToBytesMap.get(helperClass),
                        helperClass.getName(), helperClassName);
                return ReflectUtils.defineClass(helperClassName, helperClassBytes, classLoader);
            } catch (Throwable e1) {
                logger.error("arthas loggger command try to define helper class error: " + helperClassName,
                        e1);
            }
            return null;
        }
}
