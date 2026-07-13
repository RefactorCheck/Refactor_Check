public class keycloak_0163 {

        public void handleException(Throwable e) {
            logger.debug(getDetailedMessage("Exception during transaction operation."), e);
    
            if (e instanceof RollbackException) {
                e = e.getCause() != null ? e.getCause() : e;
            }
            final Throwable finalE = e;
    
            session.getKeycloakSessionFactory().getProviderFactoriesStream(ExceptionConverter.class)
                    .map(factory -> ((ExceptionConverter) factory).convert(finalE))
                    .filter(Objects::nonNull)
                    .forEach(this::rethrow);
    
            rethrow(e);
        }
    
        private void rethrow(Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            } else {
                throw new RuntimeException(throwable);
            }
        }
}
