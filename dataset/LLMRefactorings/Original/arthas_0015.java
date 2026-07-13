public class arthas_0015 {

        @Override
        public void process(CommandProcess process) {
            if (directory != null && !FileUtils.isDirectoryOrNotExist(directory)) {
                process.end(-1, directory + " :is not a directory, please check it");
                return;
            }
            Instrumentation inst = process.session().getInstrumentation();
    
            if (code == null && classLoaderClass != null) {
                List<ClassLoader> matchedClassLoaders = ClassLoaderUtils.getClassLoaderByClassName(inst, classLoaderClass);
                if (matchedClassLoaders.size() == 1) {
                    code = Integer.toHexString(matchedClassLoaders.get(0).hashCode());
                } else if (matchedClassLoaders.size() > 1) {
                    Collection<ClassLoaderVO> classLoaderVOList = ClassUtils.createClassLoaderVOList(matchedClassLoaders);
                    JadModel jadModel = new JadModel()
                            .setClassLoaderClass(classLoaderClass)
                            .setMatchedClassLoaders(classLoaderVOList);
                    process.appendResult(jadModel);
                    process.end(-1, "Found more than one classloader by class name, please specify classloader with '-c <classloader hash>'");
                    return;
                } else {
                    process.end(-1, "Can not find classloader by class name: " + classLoaderClass + ".");
                    return;
                }
            }
            
            Set<Class<?>> matchedClasses = SearchUtils.searchClassOnly(inst, classPattern, isRegEx, code);
    
            try {
                final RowAffect affect = new RowAffect();
                final ExitStatus status;
                if (matchedClasses == null || matchedClasses.isEmpty()) {
                    status = processNoMatch(process);
                } else if (matchedClasses.size() > 1) {
                    status = processMatches(process, matchedClasses);
                } else { // matchedClasses size is 1
                    // find inner classes.
                    Set<Class<?>> withInnerClasses = SearchUtils.searchClassOnly(inst,  matchedClasses.iterator().next().getName() + "$*", false, code);
                    if(withInnerClasses.isEmpty()) {
                        withInnerClasses = matchedClasses;
                    }
                    status = processExactMatch(process, affect, inst, matchedClasses, withInnerClasses);
                }
                if (!this.sourceOnly) {
                    process.appendResult(new RowAffectModel(affect));
                }
                CommandUtils.end(process, status);
            } catch (Throwable e){
                logger.error("processing error", e);
                process.end(-1, "processing error");
            }
        }
}
