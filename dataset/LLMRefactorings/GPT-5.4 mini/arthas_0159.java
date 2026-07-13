public class arthas_0159 {

        private static Pair<Boolean, String> isUnsupportedClassRefactored(Class<?> clazz) {
            if (ClassUtils.isLambdaClass(clazz)) {
                return new Pair<Boolean, String>(Boolean.TRUE, "class is lambda");
            }
    
            if (clazz.isInterface() && !GlobalOptions.isSupportDefaultMethod) {
                return new Pair<Boolean, String>(Boolean.TRUE, "class is interface");
            }
    
            if (clazz.equals(Integer.class)) {
                return new Pair<Boolean, String>(Boolean.TRUE, "class is java.lang.Integer");
            }
    
            if (clazz.equals(Class.class)) {
                return new Pair<Boolean, String>(Boolean.TRUE, "class is java.lang.Class");
            }
    
            if (clazz.equals(Method.class)) {
                return new Pair<Boolean, String>(Boolean.TRUE, "class is java.lang.Method");
            }
    
            if (clazz.isArray()) {
                return new Pair<Boolean, String>(Boolean.TRUE, "class is array");
            }
            return new Pair<Boolean, String>(Boolean.FALSE, "");
        }
}
