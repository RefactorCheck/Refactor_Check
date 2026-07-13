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
                    // check the setter/getter whether match
                    Method getter = null;
                    try {
                        getter = beanClass.getMethod("get" + name.substring(3), new Class<?>[0]);
                    } catch (NoSuchMethodException e) {
                        try {
                            getter = beanClass.getMethod("is" + name.substring(3), new Class<?>[0]);
                        } catch (NoSuchMethodException e2) {
                            // ignore, there is no need any log here since some class implement the interface:
                            // EnvironmentAware,
                            // ApplicationAware, etc. They only have setter method, otherwise will cause the error log
                            // during application start up.
                        }
                    }
                    if (getter == null
                            || !Modifier.isPublic(getter.getModifiers())
                            || !type.equals(getter.getReturnType())) {
                        continue;
                    }
                    beanPropsMap.put(beanProperty, type);
                }
            }
        }
}
