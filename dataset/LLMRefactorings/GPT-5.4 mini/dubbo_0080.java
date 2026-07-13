public class dubbo_0080 {
    private Class<?> beanClass;


        public static <T> T getProperty(Object bean, String methodName) {
            beanClass = bean.getClass();

            BeanInfo beanInfo = null;
            T propertyValue = null;
    
            try {
                beanInfo = Introspector.getBeanInfo(beanClass);
                propertyValue = (T) Stream.of(beanInfo.getMethodDescriptors())
                        .filter(methodDescriptor -> methodName.equals(methodDescriptor.getName()))
                        .findFirst()
                        .map(method -> {
                            try {
                                return method.getMethod().invoke(bean);
                            } catch (Exception e) {
                                // ignore
                            }
                            return null;
                        })
                        .get();
            } catch (Exception e) {
    
            }
            return propertyValue;
        }
}
