public class springframework_0188 {

    private void contributeHibernateHints(RuntimeHints hints, @Nullable ClassLoader classLoader, Class<?> managedClass) {
        ReflectionHints reflection = hints.reflection();

        Class<? extends Annotation> embeddableInstantiatorClass =
                loadClass("org.hibernate.annotations.EmbeddableInstantiator", classLoader);
        if (embeddableInstantiatorClass != null) {
            registerForReflection(reflection,
                    AnnotationUtils.findAnnotation(managedClass, embeddableInstantiatorClass), "value");
            ReflectionUtils.doWithFields(managedClass, field -> {
                registerForReflection(reflection,
                        AnnotationUtils.findAnnotation(field, embeddableInstantiatorClass), "value");
                registerForReflection(reflection,
                        AnnotationUtils.findAnnotation(field.getType(), embeddableInstantiatorClass), "value");
            });
            ReflectionUtils.doWithMethods(managedClass, method -> registerForReflection(reflection,
                    AnnotationUtils.findAnnotation(method, embeddableInstantiatorClass), "value"));
        }

        processHibernateAnnotation(reflection, classLoader, managedClass,
                "org.hibernate.annotations.ValueGenerationType", "generatedBy");
        processHibernateAnnotation(reflection, classLoader, managedClass,
                "org.hibernate.annotations.IdGeneratorType", "value");
        processHibernateAnnotation(reflection, classLoader, managedClass,
                "org.hibernate.annotations.AttributeBinderType", "binder");
    }

    private void processHibernateAnnotation(ReflectionHints reflection, @Nullable ClassLoader classLoader,
            Class<?> managedClass, String annotationName, String attributeName) {
        Class<? extends Annotation> annotationClass = loadClass(annotationName, classLoader);
        if (annotationClass != null) {
            ReflectionUtils.doWithFields(managedClass, field -> registerForReflection(reflection,
                    AnnotationUtils.findAnnotation(field, annotationClass), attributeName));
            ReflectionUtils.doWithMethods(managedClass, method -> registerForReflection(reflection,
                    AnnotationUtils.findAnnotation(method, annotationClass), attributeName));
        }
    }
}
