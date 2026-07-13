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
    
            synchronized (defineClassMethod) {
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
                    defineClassMethod.setAccessible((defineClassMethod.isAccessible()));
                }
            }
    
    
        }
}
