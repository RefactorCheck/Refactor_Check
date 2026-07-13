public class arthas_0081 {

        @Override
        public void process(CommandProcess process) {
            if (resolveClassLoaderHashCode(process)) {
                if (this.name != null && this.level != null) {
                    level(process);
                } else {
                    loggers(process);
                }
            }
        }

        private boolean resolveClassLoaderHashCode(CommandProcess process) {
            if (hashCode == null && classLoaderClass != null) {
                Instrumentation inst = process.session().getInstrumentation();
                List<ClassLoader> matchedClassLoaders = ClassLoaderUtils.getClassLoaderByClassName(inst,
                        classLoaderClass);
                if (matchedClassLoaders.size() == 1) {
                    hashCode = Integer.toHexString(matchedClassLoaders.get(0).hashCode());
                } else if (matchedClassLoaders.size() > 1) {
                    Collection<ClassLoaderVO> classLoaderVOList = ClassUtils
                            .createClassLoaderVOList(matchedClassLoaders);
                    LoggerModel loggerModel = new LoggerModel().setClassLoaderClass(classLoaderClass)
                            .setMatchedClassLoaders(classLoaderVOList);
                    process.appendResult(loggerModel);
                    process.end(-1,
                            "Found more than one classloader by class name, please specify classloader with '-c <classloader hash>'");
                    return false;
                } else {
                    process.end(-1, "Can not find classloader by class name: " + classLoaderClass + ".");
                    return false;
                }
            }
            return true;
        }
}
