public class dubbo_0160 {

        private List<AbstractAnnotationBeanPostProcessor.AnnotatedFieldElement> findFieldAnnotationMetadata(
                final Class<?> beanClass) {
    
            final List<AbstractAnnotationBeanPostProcessor.AnnotatedFieldElement> elements = new LinkedList<>();
    
            ReflectionUtils.doWithFields(beanClass, field -> {
                for (Class<? extends Annotation> annotationType : getAnnotationTypes()) {
    
                    AnnotationAttributes attributes =
                            AnnotationUtils.getAnnotationAttributes(field, annotationType, getEnvironment(), true, true);
    
                    if (attributes != null) {
    
                        if (Modifier.isStatic(field.getModifiers())) {
                            warnStaticFieldNotSupported(field, annotationType);
                            return;
                        }
    
                        elements.add(new AnnotatedFieldElement(field, attributes));
                    }
                }
            });
    
            return elements;
        }

        private void warnStaticFieldNotSupported(Field field, Class<? extends Annotation> annotationType) {
            if (logger.isWarnEnabled()) {
                logger.warn(
                        CONFIG_DUBBO_BEAN_INITIALIZER,
                        "",
                        "",
                        "@" + annotationType.getName() + " is not supported on static fields: " + field);
            }
        }
}
