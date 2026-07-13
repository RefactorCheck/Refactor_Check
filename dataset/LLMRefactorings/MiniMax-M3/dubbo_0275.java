public class dubbo_0275 {

        private static Object instantiateForDeserialize(
                JavaBeanDescriptor beanDescriptor, ClassLoader loader, IdentityHashMap<JavaBeanDescriptor, Object> cache) {
            if (cache.containsKey(beanDescriptor)) {
                return cache.get(beanDescriptor);
            }
    
            if (beanDescriptor.isClassType()) {
                return loadClass(loader, beanDescriptor.getClassNameProperty());
            }
    
            if (beanDescriptor.isEnumType()) {
                try {
                    Class<?> enumType = loadClass(loader, beanDescriptor.getClassName());
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
                Class<?> componentType = loadClass(loader, beanDescriptor.getClassName());
                result = Array.newInstance(componentType, beanDescriptor.propertySize());
                cache.put(beanDescriptor, result);
            } else {
                try {
                    Class<?> cl = loadClass(loader, beanDescriptor.getClassName());
                    result = instantiate(cl);
                    cache.put(beanDescriptor, result);
                } catch (Exception e) {
                    throw new RuntimeException(e.getMessage(), e);
                }
            }
    
            return result;
        }

        private static Class<?> loadClass(ClassLoader loader, String className) {
            try {
                return name2Class(loader, className);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e.getMessage(), e);
            }
        }
}
