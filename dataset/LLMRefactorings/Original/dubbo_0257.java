public class dubbo_0257 {

        protected void appendAnnotation(Class<?> annotationClass, Object annotation) {
            Method[] methods = annotationClass.getMethods();
            for (Method method : methods) {
                if (method.getDeclaringClass() != Object.class
                        && method.getDeclaringClass() != Annotation.class
                        && method.getReturnType() != void.class
                        && method.getParameterTypes().length == 0
                        && Modifier.isPublic(method.getModifiers())
                        && !Modifier.isStatic(method.getModifiers())) {
                    try {
                        String property = method.getName();
                        if ("interfaceClass".equals(property) || "interfaceName".equals(property)) {
                            property = "interface";
                        }
                        String setter = calculatePropertyToSetter(property);
                        Object value = method.invoke(annotation);
                        if (value != null && !value.equals(method.getDefaultValue())) {
                            Class<?> parameterType = ReflectUtils.getBoxedClass(method.getReturnType());
                            if ("filter".equals(property) || "listener".equals(property)) {
                                parameterType = String.class;
                                value = StringUtils.join((String[]) value, ",");
                            } else if ("parameters".equals(property)) {
                                parameterType = Map.class;
                                value = CollectionUtils.toStringMap((String[]) value);
                            }
                            try {
                                Method setterMethod = getClass().getMethod(setter, parameterType);
                                setterMethod.invoke(this, value);
                            } catch (NoSuchMethodException e) {
                                // ignore
                            }
                        }
                    } catch (Throwable e) {
                        logger.error(COMMON_REFLECTIVE_OPERATION_FAILED, "", "", e.getMessage(), e);
                    }
                }
            }
        }
}
