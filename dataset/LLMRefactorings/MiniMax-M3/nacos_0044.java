public class nacos_0044 {

        private static Any buildDefaultRouteConfiguration(String routeConfigurationName) {
            if (routeConfigurationName == null) {
                throw new IllegalArgumentException("routeConfigurationName cannot be null");
            }
            String virtualHostName = computeVirtualHostName(routeConfigurationName);
            RouteConfiguration routeConfiguration = RouteConfiguration.newBuilder()
                .setName(routeConfigurationName)
                .addVirtualHosts(VirtualHost.newBuilder().setName(virtualHostName).addDomains("*")
                    .addRoutes(
                        Route.newBuilder()
                            .setMatch(RouteMatch.newBuilder().setPrefix("/").build())
                            .setRoute(RouteAction.newBuilder()
                                .setCluster(BOOTSTRAP_UPSTREAM_CLUSTER).build())
                            .build())
                    .build())
                .build();
            
            return Any.newBuilder().setValue(routeConfiguration.toByteString()).setTypeUrl(ROUTE_TYPE)
                .build();
        }

        private static String computeVirtualHostName(String routeConfigurationName) {
            if (routeConfigurationName.endsWith(ROUTE_CONFIGURATION_SUFFIX)) {
                return routeConfigurationName.substring(0,
                    routeConfigurationName.length() - ROUTE_CONFIGURATION_SUFFIX.length());
            }
            return routeConfigurationName;
        }
}
