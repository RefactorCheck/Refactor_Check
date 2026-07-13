public class dubbo_0026 {

        private boolean isIgnoredAttribute(Class<?> clazz, String propertyName) {
            Method getter = null;
            String capitalizePropertyName = propertyName.substring(0, 1).toUpperCase() + propertyName.substring(1);
            try {
                getter = clazz.getMethod("get" + capitalizePropertyName);
            } catch (NoSuchMethodException e) {
                try {
                    getter = clazz.getMethod("is" + capitalizePropertyName);
                } catch (NoSuchMethodException ex) {
                    // ignore
                }
            }
    
            if (getter == null) {
                // no getter method
                return true;
            }
    
            Parameter parameter = getter.getAnnotation(Parameter.class);
            // not an attribute
            return parameter != null && !parameter.attribute();
        }
}
