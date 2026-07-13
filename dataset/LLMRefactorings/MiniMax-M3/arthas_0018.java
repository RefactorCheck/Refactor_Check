public class arthas_0018 {

    private static final String JAVA_LANG_PREFIX = "java.lang.";

        @Override
        public Class classForName(String className, Map context) throws ClassNotFoundException {
            Class<?> result = null;
    
            if ((result = classes.get(className)) == null) {
                try {
                    result = classLoader.loadClass(className);
                } catch (ClassNotFoundException ex) {
                    if (className.indexOf('.') == -1) {
                        String fullName = JAVA_LANG_PREFIX + className;
                        result = Class.forName(fullName);
                        classes.put(fullName, result);
                    }
                }
                if (result == null) {
                    return null;
                }
                classes.put(className, result);
            }
            return result;
        }
}
