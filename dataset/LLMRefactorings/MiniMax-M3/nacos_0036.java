public class nacos_0036 {

    private static final String LOG_PREFIX = "[VisibilityPluginManager]";

        private void registerVisibilityService(VisibilityService service, Properties allProperties) {
            String serviceName;
            try {
                serviceName = service.getVisibilityServiceName();
            } catch (Throwable ex) {
                LOGGER.warn(
                    "{} VisibilityService({}) resolve name failed, skip.",
                    LOG_PREFIX, service.getClass(), ex);
                return;
            }
            if (StringUtils.isEmpty(serviceName)) {
                LOGGER.warn(
                    "{} VisibilityService({}) has empty serviceName, skip.",
                    LOG_PREFIX, service.getClass());
                return;
            }
            Properties serviceProperties = resolveServiceProperties(allProperties, serviceName);
            try {
                service.init(serviceProperties);
            } catch (Throwable ex) {
                LOGGER.warn(
                    "{} Initialize VisibilityService({}:{}) failed, skip.",
                    LOG_PREFIX, service.getClass(), serviceName, ex);
                return;
            }
            visibilityServiceMap.put(serviceName, service);
            LOGGER.info("{} Loaded VisibilityService({}:{}) successfully.",
                LOG_PREFIX, service.getClass(), serviceName);
        }
}
