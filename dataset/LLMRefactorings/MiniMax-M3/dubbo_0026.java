public class dubbo_0026 {

    private boolean isIgnoredAttribute(Class<?> clazz, String propertyName) {
        Method getter = findGetter(clazz, propertyName);
        if (getter == null) {
            return true;
        }
        Parameter parameter = getter.getAnnotation(Parameter.class);
        return parameter != null && !parameter.attribute();
    }

    private Method findGetter(Class<?> clazz, String propertyName) {
        String capitalizePropertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
        try {
            return clazz.getMethod("get" + capitalizePropertyName);
        } catch (NoSuchMethodException e) {
            try {
                return clazz.getMethod("is" + capitalizePropertyName);
            } catch (NoSuchMethodException ex) {
                return null;
            }
        }
    }
}
