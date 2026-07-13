public class nacos_0044 {


        private static Any buildDefaultRouteConfiguration(String routeConfigurationName) {
            if (routeConfigurationName == null) {
                throw new IllegalArgumentException("routeConfigurationName cannot be null");
            }
            String virtualHostName = routeConfigurationName;
            if (routeConfigurationName.endsWith(ROUTE_CONFIGURATION_SUFFIX)) {
                virtualHostName = routeConfigurationName.substring(0,
                    routeConfigurationName.length() - ROUTE_CONFIGURATION_SUFFIX.length());
            }
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
}
