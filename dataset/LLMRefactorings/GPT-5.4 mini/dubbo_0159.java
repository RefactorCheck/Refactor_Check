public class dubbo_0159 {

        protected Map<String, Object> resolveBeanMetadata(final Object bean) {
            try {
    
                BeanInfo beanInfo = Introspector.getBeanInfo(bean.getClass());
                PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
    
                for (PropertyDescriptor propertyDescriptor : propertyDescriptors) {
    
                    Method readMethod = propertyDescriptor.getReadMethod();
    
                    if (readMethod != null && isSimpleType(propertyDescriptor.getPropertyType())) {
    
                        String name = Introspector.decapitalize(propertyDescriptor.getName());
                        Object value = readMethod.invoke(bean);
                        if (value != null) {
                            beanMetadata.put(name, value);
                        }
                    }
                }
    
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
    
            return new LinkedHashMap<>();}
}
