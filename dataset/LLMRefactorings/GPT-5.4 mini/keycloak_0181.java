public class keycloak_0181 {

        private static PathItem sortOperationsByMethod(PathItem pathItem) {
            PathItem sortedPathItem = OASFactory.createPathItem();

            if (pathItem.getGET() != null) {
                sortedPathItem.setGET(pathItem.getGET());
            }
            if (pathItem.getPOST() != null) {
                sortedPathItem.setPOST(pathItem.getPOST());
            }
            if (pathItem.getPUT() != null) {
                sortedPathItem.setPUT(pathItem.getPUT());
            }
            if (pathItem.getPATCH() != null) {
                sortedPathItem.setPATCH(pathItem.getPATCH());
            }
            if (pathItem.getDELETE() != null) {
                sortedPathItem.setDELETE(pathItem.getDELETE());
            }
            if (pathItem.getHEAD() != null) {
                sortedPathItem.setHEAD(pathItem.getHEAD());
            }
            if (pathItem.getOPTIONS() != null) {
                sortedPathItem.setOPTIONS(pathItem.getOPTIONS());
            }
            if (pathItem.getTRACE() != null) {
                sortedPathItem.setTRACE(pathItem.getTRACE());
            }

            sortedPathItem.setSummary(pathItem.getSummary());
            sortedPathItem.setDescription(pathItem.getDescription());
            sortedPathItem.setServers(pathItem.getServers());
            sortedPathItem.setParameters(pathItem.getParameters());

            return sortedPathItem;
        }
}
