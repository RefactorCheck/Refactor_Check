public class keycloak_0016 {

        @Produces
        @Readiness
        @Dependent
        public AsyncHealthCheck createHealthCheck() {
            if (ready) {
                return instance;
            }
            if (!sessionFactory.isBootstrapCompleted()) {
                return null;
            }
            synchronized (this) {
                if (ready) {
                    return instance;
                }
                instance = createHealthCheckInstance();
                ready = true;
            }
    
            return instance;
        }

        private AsyncHealthCheck createHealthCheckInstance() {
            var factory = (InfinispanConnectionProviderFactory) sessionFactory.getProviderFactory(InfinispanConnectionProvider.class);
            if (factory.isClusterHealthSupported()) {
                return new KeycloakClusterReadyHealthCheck(factory);
            }
            return null;
        }
}
