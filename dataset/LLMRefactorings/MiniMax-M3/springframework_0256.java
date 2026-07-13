public class springframework_0256 {

    private static void validate(Class target,
                                 String[] getters,
                                 String[] setters,
                                 Class[] types,
                                 Method[] getters_out,
                                 Method[] setters_out) {
        int i = -1;
        if (setters.length != types.length || getters.length != types.length) {
            throw new BulkBeanException("accessor array length must be equal type array length", i);
        }
        try {
            for (i = 0; i < types.length; i++) {
                if (getters[i] != null) {
                    getters_out[i] = validateGetter(target, getters[i], types[i], i);
                }
                if (setters[i] != null) {
                    setters_out[i] = validateSetter(target, setters[i], types[i], i);
                }
            }
        } catch (NoSuchMethodException e) {
            throw new BulkBeanException("Cannot find specified property", i);
        }
    }

    private static Method validateGetter(Class target, String getter, Class<?> type, int i) throws NoSuchMethodException {
        Method method = ReflectUtils.findDeclaredMethod(target, getter, null);
        if (method.getReturnType() != type) {
            throw new BulkBeanException("Specified type " + type +
                                        " does not match declared type " + method.getReturnType(), i);
        }
        if (Modifier.isPrivate(method.getModifiers())) {
            throw new BulkBeanException("Property is private", i);
        }
        return method;
    }

    private static Method validateSetter(Class target, String setter, Class<?> type, int i) throws NoSuchMethodException {
        Method method = ReflectUtils.findDeclaredMethod(target, setter, new Class[]{ type });
        if (Modifier.isPrivate(method.getModifiers())) {
            throw new BulkBeanException("Property is private", i);
        }
        return method;
    }
}
