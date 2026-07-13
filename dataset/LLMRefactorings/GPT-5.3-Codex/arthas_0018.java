public class arthas_0018 {
            private static final String CLASSFORNAME_VALUE = "java.lang.";


        @Override
        public Class classForName(String className, Map context) throws ClassNotFoundException {
            Class<?> result = null;
    
            if ((result = classes.get(className)) == null) {
                try {
                    result = classLoader.loadClass(className);
                } catch (ClassNotFoundException ex) {
                    if (className.indexOf('.') == -1) {
                        result = Class.forName(CLASSFORNAME_VALUE + className);
                        classes.put("java.lang." + className, result);
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
