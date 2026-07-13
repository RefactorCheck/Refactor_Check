public class keycloak_0004 {

    protected void baseLiquibaseInitialization() {
        initializeScope();
        enterLiquibaseScope();
    }

    private void initializeScope() {
        ClassLoader currentClassLoader = Thread.currentThread().getContextClassLoader();
        try {
            Thread.currentThread().setContextClassLoader(this.getClass().getClassLoader());
            Scope.setScopeManager(new ThreadLocalScopeManager());
            Scope.getCurrentScope();
        } finally {
            Thread.currentThread().setContextClassLoader(currentClassLoader);
        }
    }

    private void enterLiquibaseScope() {
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
