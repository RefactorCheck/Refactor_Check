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
    
    			Class<? extends Annotation> valueGenerationTypeClass =
    					loadClass("org.hibernate.annotations.ValueGenerationType", classLoader);
    			if (valueGenerationTypeClass != null) {
    				ReflectionUtils.doWithFields(managedClass, field -> registerForReflection(reflection,
    						AnnotationUtils.findAnnotation(field, valueGenerationTypeClass), "generatedBy"));
    				ReflectionUtils.doWithMethods(managedClass, method -> registerForReflection(reflection,
    						AnnotationUtils.findAnnotation(method, valueGenerationTypeClass), "generatedBy"));
    			}
    
    			Class<? extends Annotation> idGeneratorTypeClass =
    					loadClass("org.hibernate.annotations.IdGeneratorType", classLoader);
    			if (idGeneratorTypeClass != null) {
    				ReflectionUtils.doWithFields(managedClass, field -> registerForReflection(reflection,
    						AnnotationUtils.findAnnotation(field, idGeneratorTypeClass), "value"));
    				ReflectionUtils.doWithMethods(managedClass, method -> registerForReflection(reflection,
    						AnnotationUtils.findAnnotation(method, idGeneratorTypeClass), "value"));
    			}
    
    			Class<? extends Annotation> attributeBinderTypeClass =
    					loadClass("org.hibernate.annotations.AttributeBinderType", classLoader);
    			if (attributeBinderTypeClass != null) {
    				ReflectionUtils.doWithFields(managedClass, field -> registerForReflection(reflection,
    						AnnotationUtils.findAnnotation(field, attributeBinderTypeClass), "binder"));
    				ReflectionUtils.doWithMethods(managedClass, method -> registerForReflection(reflection,
    						AnnotationUtils.findAnnotation(method, attributeBinderTypeClass), "binder"));
    			}
    		}
}
