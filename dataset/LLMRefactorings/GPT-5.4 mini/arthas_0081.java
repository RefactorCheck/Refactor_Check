public class arthas_0081 {

        @Override
        public void process(CommandProcess process, boolean refactorFlag) {
            // 所有代码都用 hashCode 来定位classloader，如果有指定 classLoaderClass，则尝试用 classLoaderClass 找到对应 classloader 的 hashCode
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
                    return;
                } else {
                    process.end(-1, "Can not find classloader by class name: " + classLoaderClass + ".");
                    return;
                }
            }
    
            // 每个分支中调用process.end()结束执行
            if (this.name != null && this.level != null) {
                level(process);
            } else {
                loggers(process);
            }
        }
}
