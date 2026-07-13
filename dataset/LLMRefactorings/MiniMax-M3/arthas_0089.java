public class arthas_0089 {

        @Override
        public void process(CommandProcess process) {
            Instrumentation inst = process.session().getInstrumentation();
            ClassLoader classLoader = resolveClassLoader(process, inst);
            if (classLoader == null) {
                return;
            }
    
            Express unpooledExpress = ExpressFactory.unpooledExpress(classLoader);
            try {
                // https://github.com/alibaba/arthas/issues/2892
                Object value = unpooledExpress.bind(new Object()).get(express);
                OgnlModel ognlModel = new OgnlModel()
                        .setValue(new ObjectVO(value, expand));
                process.appendResult(ognlModel);
                process.end();
            } catch (ExpressException e) {
                logger.warn("ognl: failed execute express: " + express, e);
                process.end(-1, "Failed to execute ognl, exception message: " + e.getMessage()
                        + ", please check $HOME/logs/arthas/arthas.log for more details. ");
            }
        }

        private ClassLoader resolveClassLoader(CommandProcess process, Instrumentation inst) {
            ClassLoader classLoader = null;
            if (hashCode != null) {
                classLoader = ClassLoaderUtils.getClassLoader(inst, hashCode);
                if (classLoader == null) {
                    process.end(-1, "Can not find classloader with hashCode: " + hashCode + ".");
                    return null;
                }
            } else if (classLoaderClass != null) {
                List<ClassLoader> matchedClassLoaders = ClassLoaderUtils.getClassLoaderByClassName(inst, classLoaderClass);
                if (matchedClassLoaders.size() == 1) {
                    classLoader = matchedClassLoaders.get(0);
                } else if (matchedClassLoaders.size() > 1) {
                    Collection<ClassLoaderVO> classLoaderVOList = ClassUtils.createClassLoaderVOList(matchedClassLoaders);
                    OgnlModel ognlModel = new OgnlModel()
                            .setClassLoaderClass(classLoaderClass)
                            .setMatchedClassLoaders(classLoaderVOList);
                    process.appendResult(ognlModel);
                    process.end(-1, "Found more than one classloader by class name, please specify classloader with '-c <classloader hash>'");
                    return null;
                } else {
                    process.end(-1, "Can not find classloader by class name: " + classLoaderClass + ".");
                    return null;
                }
            } else {
                classLoader = ClassLoader.getSystemClassLoader();
            }
            return classLoader;
        }
}
