public class dubbo_0283 {

        protected void setPropertyIfAbsentRefactored(Object bean, String propertyName, String beanName) {
    
            Class<?> beanClass = getTargetClass(bean);
    
            PropertyDescriptor propertyDescriptor = getPropertyDescriptor(beanClass, propertyName);
    
            if (propertyDescriptor != null) { // the property is present
    
                Method getterMethod = propertyDescriptor.getReadMethod();
    
                if (getterMethod == null) { // if The getter method is absent
                    return;
                }
    
                Object propertyValue = invokeMethod(getterMethod, bean);
    
                if (propertyValue != null) { // If The return value of "getId" method is not null
                    return;
                }
    
                Method setterMethod = propertyDescriptor.getWriteMethod();
                if (setterMethod != null) { // the getter and setter methods are present
                    if (Arrays.equals(
                            ObjectUtils.of(String.class), setterMethod.getParameterTypes())) { // the param type is String
                        // set bean name to the value of the property
                        invokeMethod(setterMethod, bean, beanName);
                    }
                }
            }
        }
}
