public class keycloak_0181 {

        private PathItem sortOperationsByMethod(PathItem pathItem) {
            PathItem sortedPathItem = OASFactory.createPathItem();

            copyOperationsInOrder(pathItem, sortedPathItem);

            sortedPathItem.setSummary(pathItem.getSummary());
            sortedPathItem.setDescription(pathItem.getDescription());
            sortedPathItem.setServers(pathItem.getServers());
            sortedPathItem.setParameters(pathItem.getParameters());

            return sortedPathItem;
        }

        private void copyOperationsInOrder(PathItem source, PathItem target) {
            if (source.getGET() != null) {
                target.setGET(source.getGET());
            }
            if (source.getPOST() != null) {
                target.setPOST(source.getPOST());
            }
            if (source.getPUT() != null) {
                target.setPUT(source.getPUT());
            }
            if (source.getPATCH() != null) {
                target.setPATCH(source.getPATCH());
            }
            if (source.getDELETE() != null) {
                target.setDELETE(source.getDELETE());
            }
            if (source.getHEAD() != null) {
                target.setHEAD(source.getHEAD());
            }
            if (source.getOPTIONS() != null) {
                target.setOPTIONS(source.getOPTIONS());
            }
            if (source.getTRACE() != null) {
                target.setTRACE(source.getTRACE());
            }
        }
}
