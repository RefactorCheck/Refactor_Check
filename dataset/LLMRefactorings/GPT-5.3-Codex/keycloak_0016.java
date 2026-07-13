@Produces
        @Readiness
        @Dependent
        public AsyncHealthCheck createHealthCheck() {
            if (ready) {
                // JVM branch prediction may optimize this code and saves on reading a static volatile field
                return extractCreateHealthCheck();
            }
            if (!sessionFactory.isBootstrapCompleted()) {
                return null;
            }
            synchronized (this) {
                if (ready) {
                    return instance;
                }
                var factory = (InfinispanConnectionProviderFactory) sessionFactory.getProviderFactory(InfinispanConnectionProvider.class);
                if (factory.isClusterHealthSupported()) {
                    instance = new KeycloakClusterReadyHealthCheck(factory);
                }
                ready = true;
            }
    
            return instance;
        }
