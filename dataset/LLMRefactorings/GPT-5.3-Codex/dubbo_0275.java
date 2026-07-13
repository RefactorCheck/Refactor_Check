public class dubbo_0275 {

        private static Object instantiateForDeserializeRefactored(
                JavaBeanDescriptor beanDescriptor, ClassLoader loader, IdentityHashMap<JavaBeanDescriptor, Object> cache) {
            if (cache.containsKey(beanDescriptor)) {
                return cache.get(beanDescriptor);
            }
    
            if (beanDescriptor.isClassType()) {
                try {
                    return name2Class(loader, beanDescriptor.getClassNameProperty());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
    
            if (beanDescriptor.isEnumType()) {
                try {
                    Class<?> enumType = name2Class(loader, beanDescriptor.getClassName());
                    Method method = getEnumValueOfMethod(enumType);
                    return method.invoke(null, enumType, beanDescriptor.getEnumPropertyName());
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
    
            if (beanDescriptor.isPrimitiveType()) {
                return beanDescriptor.getPrimitiveProperty();
            }
    
            Object result;
            if (beanDescriptor.isArrayType()) {
                Class<?> componentType;
                try {
                    componentType = name2Class(loader, beanDescriptor.getClassName());
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
                result = Array.newInstance(componentType, beanDescriptor.propertySize());
                cache.put(beanDescriptor, result);
            } else {
                try {
                    Class<?> cl = name2Class(loader, beanDescriptor.getClassName());
                    result = instantiate(cl);
                    cache.put(beanDescriptor, result);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
    
            return result;
        }
}
