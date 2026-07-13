public class arthas_0025 {

        public static EnhancerAffectVO createEnhancerAffectVO(EnhancerAffect affect) {
            if (affect == null) {
                return new EnhancerAffectVO(-1, 0, 0, -1);
            }
            
            EnhancerAffectVO vo = new EnhancerAffectVO(
                affect.cost(),
                affect.mCnt(),
                affect.cCnt(),
                affect.getListenerId()
            );
            vo.setThrowable(affect.getThrowable());
            vo.setOverLimitMsg(affect.getOverLimitMsg());
            
            if (GlobalOptions.isDump) {
                List<String> classDumpFiles = new ArrayList<String>();
                for (File classDumpFile : affect.getClassDumpFiles()) {
                    classDumpFiles.add(classDumpFile.getAbsolutePath());
                }
                vo.setClassDumpFiles(classDumpFiles);
            }
    
            if (GlobalOptions.verbose) {
                List<String> methods = new ArrayList<String>();
                methods.addAll(affect.getMethods());
                vo.setMethods(methods);
            }
            
            return vo;
        }
}
