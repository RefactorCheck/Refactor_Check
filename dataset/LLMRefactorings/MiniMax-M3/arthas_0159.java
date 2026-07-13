public class arthas_0159 {

        private static Pair<Boolean, String> isUnsupportedClass(Class<?> clazz) {
            if (ClassUtils.isLambdaClass(clazz)) {
                return createUnsupportedPair("class is lambda");
            }
    
            if (clazz.isInterface() && !GlobalOptions.isSupportDefaultMethod) {
                return createUnsupportedPair("class is interface");
            }
    
            if (clazz.equals(Integer.class)) {
                return createUnsupportedPair("class is java.lang.Integer");
            }
    
            if (clazz.equals(Class.class)) {
                return createUnsupportedPair("class is java.lang.Class");
            }
    
            if (clazz.equals(Method.class)) {
                return createUnsupportedPair("class is java.lang.Method");
            }
    
            if (clazz.isArray()) {
                return createUnsupportedPair("class is array");
            }
            return new Pair<Boolean, String>(Boolean.FALSE, "");
        }
    
        private static Pair<Boolean, String> createUnsupportedPair(String reason) {
            return new Pair<Boolean, String>(Boolean.TRUE, reason);
        }
}
