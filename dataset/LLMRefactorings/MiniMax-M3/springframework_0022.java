public class springframework_0022 {

    private boolean shouldRemoveTrailingServletPathSlash(HttpServletRequest request) {
        if (request.getAttribute(WEBSPHERE_URI_ATTRIBUTE) == null) {
            return false;
        }
        Boolean flagToUse = websphereComplianceFlag;
        if (flagToUse == null) {
            flagToUse = resolveWebSphereComplianceFlag();
            websphereComplianceFlag = flagToUse;
        }
        return !flagToUse;
    }

    private Boolean resolveWebSphereComplianceFlag() {
        ClassLoader classLoader = UrlPathHelper.class.getClassLoader();
        String className = "com.ibm.ws.webcontainer.WebContainer";
        String methodName = "getWebContainerProperties";
        String propName = "com.ibm.ws.webcontainer.removetrailingservletpathslash";
        boolean flag = false;
        try {
            Class<?> cl = classLoader.loadClass(className);
            Properties prop = (Properties) cl.getMethod(methodName).invoke(null);
            flag = Boolean.parseBoolean(prop.getProperty(propName));
        }
        catch (Throwable ex) {
            if (logger.isDebugEnabled()) {
                logger.debug("Could not introspect WebSphere web container properties: " + ex);
            }
        }
        return flag;
    }
}
