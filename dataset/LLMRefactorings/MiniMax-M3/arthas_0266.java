public class arthas_0266 {

    public static String renderEnhancerAffect(EnhancerAffectVO affectVO) {
        final StringBuilder infoSB = new StringBuilder();
        appendClassDumpFiles(infoSB, affectVO.getClassDumpFiles());
        appendAffectedMethods(infoSB, affectVO.getMethods());

        infoSB.append(format("Affect(class count: %d , method count: %d) cost in %s ms, listenerId: %d",
                affectVO.getClassCount(),
                affectVO.getMethodCount(),
                affectVO.getCost(),
                affectVO.getListenerId()));
        if (!StringUtils.isEmpty(affectVO.getOverLimitMsg())) {
            infoSB.append("\n" + affectVO.getOverLimitMsg());
        }
        if (affectVO.getThrowable() != null) {
            infoSB.append("\nEnhance error! exception: ").append(affectVO.getThrowable());
        }
        infoSB.append("\n");

        return infoSB.toString();
    }

    private static void appendClassDumpFiles(StringBuilder infoSB, List<String> classDumpFiles) {
        if (classDumpFiles != null) {
            for (String classDumpFile : classDumpFiles) {
                infoSB.append("[dump: ").append(classDumpFile).append("]\n");
            }
        }
    }

    private static void appendAffectedMethods(StringBuilder infoSB, List<String> methods) {
        if (methods != null) {
            for (String method : methods) {
                infoSB.append("[Affect method: ").append(method).append("]\n");
            }
        }
    }
}
