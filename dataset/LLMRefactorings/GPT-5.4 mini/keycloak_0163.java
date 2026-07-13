public class keycloak_0163 {

        public void handleException(Throwable e) {
            logger.debug(getDetailedMessage("Exception during transaction operation."), e);

            if (e instanceof RollbackException) {
                e = e.getCause() != null ? e.getCause() : e;
            }

            session.getKeycloakSessionFactory().getProviderFactoriesStream(ExceptionConverter.class)
                    .map(factory -> ((ExceptionConverter) factory).convert(e))
                    .filter(Objects::nonNull)
                    .forEach(this::rethrow);

            rethrow(e);
        }

        private void rethrow(Throwable throwable) {
            if (throwable instanceof RuntimeException) {
                throw (RuntimeException) throwable;
            }
            throw new RuntimeException(throwable);
        }
}
