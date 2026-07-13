public class dubbo_0188 {

        private ConfigCenterConfig registryAsConfigCenter(RegistryConfig registryConfig) {
            String protocol = registryConfig.getProtocol();
            Integer port = registryConfig.getPort();
            URL url = URL.valueOf(registryConfig.getAddress(), registryConfig.getScopeModel());
            String id = "config-center-" + protocol + "-" + url.getHost() + "-" + port;
            ConfigCenterConfig cc = new ConfigCenterConfig();
            cc.setId(id);
            cc.setScopeModel(applicationModel);
            if (cc.getParameters() == null) {
                cc.setParameters(new HashMap<>());
            }
            if (CollectionUtils.isNotEmptyMap(registryConfig.getParameters())) {
                cc.getParameters().putAll(registryConfig.getParameters()); // copy the parameters
            }
            cc.getParameters().put(CLIENT_KEY, registryConfig.getClient());
            cc.setProtocol(protocol);
            cc.setPort(port);
            if (StringUtils.isNotEmpty(registryConfig.getGroup())) {
                cc.setGroup(registryConfig.getGroup());
            }
            cc.setAddress(getRegistryCompatibleAddress(registryConfig));
            cc.setNamespace(registryConfig.getGroup());
            cc.setUsername(registryConfig.getUsername());
            cc.setPassword(registryConfig.getPassword());
            if (registryConfig.getTimeout() != null) {
                cc.setTimeout(registryConfig.getTimeout().longValue());
            }
            cc.setHighestPriority(false);
            return cc;
        }
}
