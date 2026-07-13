public class dubbo_0151 {

        private static void getPropertyMap(Class<?> beanClass, Map<String, Class> beanPropsMap) {
            for (Method setter : beanClass.getMethods()) {
                String name = setter.getName();
                if (name.length() > 3
                        && name.startsWith("set")
                        && Modifier.isPublic(setter.getModifiers())
                        && setter.getParameterTypes().length == 1) {
                    Class<?> type = setter.getParameterTypes()[0];
                    String beanProperty = name.substring(3, 4).toLowerCase() + name.substring(4);
                    Method getter = findGetter(beanClass, name);
                    if (getter == null
                            || !Modifier.isPublic(getter.getModifiers())
                            || !type.equals(getter.getReturnType())) {
                        continue;
                    }
                    beanPropsMap.put(beanProperty, type);
                }
            }
        }

        private static Method findGetter(Class<?> beanClass, String setterName) {
            String propertyName = setterName.substring(3);
            try {
                return beanClass.getMethod("get" + propertyName, new Class<?>[0]);
            } catch (NoSuchMethodException e) {
                try {
                    return beanClass.getMethod("is" + propertyName, new Class<?>[0]);
                } catch (NoSuchMethodException e2) {
                    return null;
                }
            }
        }
}
