public class dubbo_0055 {

        protected boolean verifyMethodConfig(
                MethodConfig methodConfig, Class<?> interfaceClass, boolean ignoreInvalidMethodConfig) {
            String methodName = methodConfig.getName();
            if (StringUtils.isEmpty(methodName)) {
                String msg = "<dubbo:method> name attribute is required! Please check: " + "<dubbo:service interface=\""
                        + interfaceName + "\" ... >" + "<dubbo:method name=\"\" ... /></<dubbo:reference>";
                if (ignoreInvalidMethodConfig) {
                    logger.warn(CONFIG_NO_METHOD_FOUND, "", "", msg);
                    return false;
                } else {
                    throw new IllegalStateException(msg);
                }
            }
    
            boolean hasMethod = Arrays.stream(interfaceClass.getMethods())
                    .anyMatch(method -> method.getName().equals(methodName));
            if (!hasMethod) {
                String msg = "Found invalid method config, the interface " + interfaceClass.getName()
                        + " not found method \"" + methodName + "\" : [" + methodConfig + "]";
                if (ignoreInvalidMethodConfig) {
                    logger.warn(CONFIG_NO_METHOD_FOUND, "", "", msg);
                    return false;
                } else {
                    if (!isNeedCheckMethod()) {
                        msg = "Generic call: " + msg;
                        logger.warn(CONFIG_NO_METHOD_FOUND, "", "", msg);
                    } else {
                        throw new IllegalStateException(msg);
                    }
                }
            }
            return true;
        }
}
