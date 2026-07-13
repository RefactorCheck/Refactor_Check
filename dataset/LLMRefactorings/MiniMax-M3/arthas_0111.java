public class arthas_0111 {

        public static Class<?> defineClass(
                final ClassLoader targetClassLoader,
                final String className,
                final byte[] classByteArray) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
    
            final Method defineClassMethod = ClassLoader.class.getDeclaredMethod(
                    "defineClass",
                    String.class,
                    byte[].class,
                    int.class,
                    int.class
            );
    
            return invokeDefineClassMethod(defineClassMethod, targetClassLoader, className, classByteArray);
        }

        private static Class<?> invokeDefineClassMethod(
                final Method defineClassMethod,
                final ClassLoader targetClassLoader,
                final String className,
                final byte[] classByteArray) throws InvocationTargetException, IllegalAccessException {
            synchronized (defineClassMethod) {
                final boolean acc = defineClassMethod.isAccessible();
                try {
                    defineClassMethod.setAccessible(true);
                    return (Class<?>) defineClassMethod.invoke(
                            targetClassLoader,
                            className,
                            classByteArray,
                            0,
                            classByteArray.length
                    );
                } finally {
                    defineClassMethod.setAccessible(acc);
                }
            }
        }
}
