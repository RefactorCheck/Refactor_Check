public class dubbo_0080 {

    public static <T> T getProperty(Object bean, String methodName) {
        Class<?> beanClass = bean.getClass();
        BeanInfo beanInfo = null;
        T propertyValue = null;

        try {
            beanInfo = Introspector.getBeanInfo(beanClass);
            propertyValue = (T) Stream.of(beanInfo.getMethodDescriptors())
                    .filter(methodDescriptor -> methodName.equals(methodDescriptor.getName()))
                    .findFirst()
                    .map(method -> invokeMethod(bean, method))
                    .get();
        } catch (Exception e) {

        }
        return propertyValue;
    }

    private static Object invokeMethod(Object bean, MethodDescriptor methodDescriptor) {
        try {
            return methodDescriptor.getMethod().invoke(bean);
        } catch (Exception e) {
            // ignore
        }
        return null;
    }
}
