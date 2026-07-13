public class nacos_0036 {


        private void registerVisibilityServiceRefactored(VisibilityService service, Properties allProperties) {
            String serviceName;
            try {
                serviceName = service.getVisibilityServiceName();
            } catch (Throwable ex) {
                LOGGER.warn(
                    "[VisibilityPluginManager] VisibilityService({}) resolve name failed, skip.",
                    service.getClass(), ex);
                return;
            }
            if (StringUtils.isEmpty(serviceName)) {
                LOGGER.warn(
                    "[VisibilityPluginManager] VisibilityService({}) has empty serviceName, skip.",
                    service.getClass());
                return;
            }
            Properties serviceProperties = resolveServiceProperties(allProperties, serviceName);
            try {
                service.init(serviceProperties);
            } catch (Throwable ex) {
                LOGGER.warn(
                    "[VisibilityPluginManager] Initialize VisibilityService({}:{}) failed, skip.",
                    service.getClass(), serviceName, ex);
                return;
            }
            visibilityServiceMap.put(serviceName, service);
            LOGGER.info("[VisibilityPluginManager] Loaded VisibilityService({}:{}) successfully.",
                service.getClass(), serviceName);
        
        }
}
