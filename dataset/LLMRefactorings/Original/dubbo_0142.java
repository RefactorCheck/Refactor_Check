public class dubbo_0142 {

        private static Class<?> generateMethodParameterClass(Class<?> clazz, Method method, String parameterClassName)
                throws Exception {
            ClassPool pool = ClassGenerator.getClassPool(clazz.getClassLoader());
            synchronized (parameterClassName.intern()) {
                CtClass ctClass = null;
                try {
                    ctClass = pool.getCtClass(parameterClassName);
                } catch (NotFoundException ignore) {
                }
    
                if (null == ctClass) {
                    ctClass = pool.makeClass(parameterClassName);
                    ClassFile classFile = ctClass.getClassFile();
                    ctClass.addConstructor(CtNewConstructor.defaultConstructor(pool.getCtClass(parameterClassName)));
                    // parameter fields
                    Parameter[] parameters = method.getParameters();
                    Annotation[][] parameterAnnotations = method.getParameterAnnotations();
                    for (int i = 0; i < parameters.length; i++) {
                        Annotation[] annotations = parameterAnnotations[i];
                        AnnotationsAttribute attribute =
                                new AnnotationsAttribute(classFile.getConstPool(), AnnotationsAttribute.visibleTag);
                        for (Annotation annotation : annotations) {
                            if (annotation.annotationType().isAnnotationPresent(Constraint.class)) {
                                javassist.bytecode.annotation.Annotation ja = new javassist.bytecode.annotation.Annotation(
                                        classFile.getConstPool(),
                                        pool.getCtClass(annotation.annotationType().getName()));
                                Method[] members = annotation.annotationType().getMethods();
                                for (Method member : members) {
                                    if (Modifier.isPublic(member.getModifiers())
                                            && member.getParameterTypes().length == 0
                                            && member.getDeclaringClass() == annotation.annotationType()) {
                                        Object value = member.invoke(annotation);
                                        if (null != value) {
                                            MemberValue memberValue = createMemberValue(
                                                    classFile.getConstPool(),
                                                    pool.get(member.getReturnType().getName()),
                                                    value);
                                            ja.addMemberValue(member.getName(), memberValue);
                                        }
                                    }
                                }
                                attribute.addAnnotation(ja);
                            }
                        }
                        Parameter parameter = parameters[i];
                        Class<?> type = parameter.getType();
                        String fieldName = parameter.getName();
                        CtField ctField = CtField.make(
                                "public " + type.getCanonicalName() + " " + fieldName + ";",
                                pool.getCtClass(parameterClassName));
                        ctField.getFieldInfo().addAttribute(attribute);
                        ctClass.addField(ctField);
                    }
                    return pool.toClass(ctClass, clazz, clazz.getClassLoader(), clazz.getProtectionDomain());
                } else {
                    return Class.forName(parameterClassName, true, clazz.getClassLoader());
                }
            }
        }
}
