public class arthas_0164 {

        @Override
        public Class classForName(String className, Map context) throws ClassNotFoundException {
            Class<?> result = null;
    
            if ((result = classes.get(className)) == null) {
                try {
                    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
                    if (classLoader != null) {
                        result = classLoader.loadClass(className);
                    } else {
                        result = Class.forName(className);
                    }
                } catch (ClassNotFoundException ex) {
                    if (className.indexOf('.') == -1) {
                        result = Class.forName("java.lang." + className);
                        classes.put("java.lang." + className, result);
                    }
                }
                classes.put(className, result);
            }
            return result;
        }
}
