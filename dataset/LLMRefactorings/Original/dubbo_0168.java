public class dubbo_0168 {

        @Override
        public Class<?> doCompile(Class<?> neighbor, ClassLoader classLoader, String name, String source) throws Throwable {
            CtClassBuilder builder = new CtClassBuilder();
            builder.setClassName(name);
    
            // process imported classes
            Matcher matcher = IMPORT_PATTERN.matcher(source);
            while (matcher.find()) {
                builder.addImports(matcher.group(1).trim());
            }
    
            // process extended super class
            matcher = EXTENDS_PATTERN.matcher(source);
            if (matcher.find()) {
                builder.setSuperClassName(matcher.group(1).trim());
            }
    
            // process implemented interfaces
            matcher = IMPLEMENTS_PATTERN.matcher(source);
            if (matcher.find()) {
                String[] ifaces = matcher.group(1).trim().split("\\,");
                Arrays.stream(ifaces).forEach(i -> builder.addInterface(i.trim()));
            }
    
            // process constructors, fields, methods
            String body = source.substring(source.indexOf('{') + 1, source.length() - 1);
            String[] methods = METHODS_PATTERN.split(body);
            String className = ClassUtils.getSimpleClassName(name);
            Arrays.stream(methods).map(String::trim).filter(m -> !m.isEmpty()).forEach(method -> {
                if (method.startsWith(className)) {
                    builder.addConstructor("public " + method);
                } else if (FIELD_PATTERN.matcher(method).matches()) {
                    builder.addField("private " + method);
                } else {
                    builder.addMethod("public " + method);
                }
            });
    
            // compile
            CtClass cls = builder.build(classLoader);
    
            ClassPool cp = cls.getClassPool();
            if (classLoader == null) {
                classLoader = cp.getClassLoader();
            }
            cp.insertClassPath(new LoaderClassPath(classLoader));
            cp.insertClassPath(new DubboLoaderClassPath());
    
            try {
                return cp.toClass(cls, neighbor, classLoader, JavassistCompiler.class.getProtectionDomain());
            } catch (Throwable t) {
                if (!(t instanceof CannotCompileException)) {
                    return cp.toClass(cls, classLoader, JavassistCompiler.class.getProtectionDomain());
                }
                throw t;
            }
        }
}
