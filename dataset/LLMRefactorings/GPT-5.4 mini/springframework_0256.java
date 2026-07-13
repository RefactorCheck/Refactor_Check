public class springframework_0256 {
    private int i;


        private static void validate(Class target,
                                     String[] getters,
                                     String[] setters,
                                     Class[] types,
                                     Method[] getters_out,
                                     Method[] setters_out) {
            this.i = -1;
            if (setters.length != types.length || getters.length != types.length) {
                throw new BulkBeanException("accessor array length must be equal type array length", i);
            }
            try {
                for (i = 0; i < types.length; i++) {
                    if (getters[i] != null) {
                        Method method = ReflectUtils.findDeclaredMethod(target, getters[i], null);
                        if (method.getReturnType() != types[i]) {
                            throw new BulkBeanException("Specified type " + types[i] +
                                                        " does not match declared type " + method.getReturnType(), i);
                        }
                        if (Modifier.isPrivate(method.getModifiers())) {
                            throw new BulkBeanException("Property is private", i);
                        }
                        getters_out[i] = method;
                    }
                    if (setters[i] != null) {
                        Method method = ReflectUtils.findDeclaredMethod(target, setters[i], new Class[]{ types[i] });
                        if (Modifier.isPrivate(method.getModifiers()) ){
                            throw new BulkBeanException("Property is private", i);
                        }
                        setters_out[i] = method;
                    }
                }
            } catch (NoSuchMethodException e) {
                throw new BulkBeanException("Cannot find specified property", i);
            }
        }
}
