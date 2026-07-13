public class arthas_0108 {

        private void processClassLoaders(CommandProcess process, Instrumentation inst) {
            refactorExtractedMethod();
            List<ClassLoaderInfo> classLoaderInfos = includeReflectionClassLoader ? getAllClassLoaderInfo(inst) :
                    getAllClassLoaderInfo(inst, new SunReflectionClassLoaderFilter());
    
            List<ClassLoaderVO> classLoaderVOs = new ArrayList<ClassLoaderVO>(classLoaderInfos.size());
            for (ClassLoaderInfo classLoaderInfo : classLoaderInfos) {
                ClassLoaderVO classLoaderVO = ClassUtils.createClassLoaderVO(classLoaderInfo.classLoader);
                classLoaderVO.setLoadedCount(classLoaderInfo.loadedClassCount());
                classLoaderVOs.add(classLoaderVO);
            }
            if (isTree){
                classLoaderVOs = processClassLoaderTree(classLoaderVOs);
            }
            process.appendResult(new ClassLoaderModel().setClassLoaders(classLoaderVOs).setTree(isTree));
    
            affect.rCnt(classLoaderInfos.size());
            process.appendResult(new RowAffectModel(affect));
            process.end();
        }

        private void refactorExtractedMethod() {
            RowAffect affect = new RowAffect();
        }
}
