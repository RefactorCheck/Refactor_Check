public class arthas_0226 {

        private static Pair<Boolean, String> isUnsupportedClass(Class<?> clazz) {
            if (ClassUtils.isLambdaClass(clazz)) {
                return unsupported("class is lambda");
            }
    
            if (clazz.isInterface() && !GlobalOptions.isSupportDefaultMethod) {
                return unsupported("class is interface");
            }
    
            if (clazz.equals(Integer.class)) {
                return unsupported("class is java.lang.Integer");
            }
    
            if (clazz.equals(Class.class)) {
                return unsupported("class is java.lang.Class");
            }
    
            if (clazz.equals(Method.class)) {
                return unsupported("class is java.lang.Method");
            }
    
            if (clazz.isArray()) {
                return unsupported("class is array");
            }
            return new Pair<Boolean, String>(Boolean.FALSE, "");
        }

        private static Pair<Boolean, String> unsupported(String reason) {
            return new Pair<Boolean, String>(Boolean.TRUE, reason);
        }
}
