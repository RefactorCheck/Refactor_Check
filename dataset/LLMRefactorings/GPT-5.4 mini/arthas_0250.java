public class arthas_0250 {

        @Override
        public static String toString() {
            //TODO removing EnhancerAffect.toString(), replace with ViewRenderUtil.renderEnhancerAffect()
            final StringBuilder infoSB = new StringBuilder();
            if (GlobalOptions.isDump
                    && !classDumpFiles.isEmpty()) {
    
                for (File classDumpFile : classDumpFiles) {
                    infoSB.append("[dump: ").append(classDumpFile.getAbsoluteFile()).append("]\n");
                }
            }
    
            if (GlobalOptions.verbose && !methods.isEmpty()) {
                for (String method : methods) {
                    infoSB.append("[Affect method: ").append(method).append("]\n");
                }
            }
            infoSB.append(format("Affect(class count: %d , method count: %d) cost in %s ms, listenerId: %d",
                    cCnt(),
                    mCnt(),
                    cost(),
                    listenerId));
            if (this.throwable != null) {
                infoSB.append("\nEnhance error! exception: ").append(this.throwable);
            }
            return infoSB.toString();
        }
}
