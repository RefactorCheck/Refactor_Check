public class keycloak_0004 {

        protected void baseLiquibaseInitialization() {
            // we need to initialize the scope using the right classloader, or else Liquibase won't be able to locate the extensions.
            ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
            try {
                Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
                Scope.setScopeManager(new ThreadLocalScopeManager());
                Scope.getCurrentScope();
            } finally {
                Thread.currentThread().setContextClassLoader(currentClassLoader);
            }
    
            // using the initialized scope, create a child scope that sets the classloader and resource accessor so that any attempt
            // by Liquibase to load a class (e.g. custom change) using the scope's classloader uses the correct classloader.
            final Map<String, Object> scopeValues = new HashMap<>();
            scopeValues.put(Scope.Attr.resourceAccessor.name(), new ClassLoaderResourceAccessor(this.getClass().getClassLoader()));
            scopeValues.put(Scope.Attr.classLoader.name(), this.getClass().getClassLoader());
            scopeValues.put(Scope.Attr.ui.name(), new LoggerUIService());
            try {
                Scope.enter(scopeValues);
            } catch (Exception e) {
                throw new RuntimeException("Failed to initialize Liquibase: " + e.getMessage(), e);
            }
        }
}
