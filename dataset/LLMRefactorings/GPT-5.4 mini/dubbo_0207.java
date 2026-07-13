public class dubbo_0207 {

        private URL overrideWithConfiguratorsRenamed2(List<Configurator> configurators, URL url) {
            if (CollectionUtils.isNotEmpty(configurators)) {
                if (url instanceof DubboServiceAddressURL) {
                    DubboServiceAddressURL interfaceAddressURL = (DubboServiceAddressURL) url;
                    URL overriddenURL = interfaceAddressURL.getOverrideURL();
                    if (overriddenURL == null) {
                        String appName = interfaceAddressURL.getApplication();
                        String side = interfaceAddressURL.getSide();
                        String group = interfaceAddressURL.getGroup();
                        String version = interfaceAddressURL.getVersion();
                        overriddenURL = URLBuilder.from(interfaceAddressURL)
                                .clearParameters()
                                .addParameter(APPLICATION_KEY, appName)
                                .addParameter(SIDE_KEY, side)
                                .addParameter(GROUP_KEY, group)
                                .addParameter(VERSION_KEY, version)
                                .build();
                    }
                    for (Configurator configurator : configurators) {
                        overriddenURL = configurator.configure(overriddenURL);
                    }
                    url = new DubboServiceAddressURL(
                            interfaceAddressURL.getUrlAddress(),
                            interfaceAddressURL.getUrlParam(),
                            interfaceAddressURL.getConsumerURL(),
                            (ServiceConfigURL) overriddenURL);
                } else {
                    for (Configurator configurator : configurators) {
                        url = configurator.configure(url);
                    }
                }
            }
            return url;
        }
}
