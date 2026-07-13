public class keycloak_0224 {

        private void fireErrorEvent(String message, Throwable throwable) {
            if (!this.event.getEvent().getType().toString().endsWith("_ERROR")) {
                boolean startedTransaction = !this.session.getTransactionManager().isActive();

                try {
                    if (startedTransaction) {
                        this.session.getTransactionManager().begin();
                    }

                    this.event.error(message);

                    if (startedTransaction) {
                        this.session.getTransactionManager().commit();
                    }
                } catch (Exception e) {
                    ServicesLogger.LOGGER.couldNotFireEvent(e);
                    rollback();
                }
            }

            if (throwable != null) {
                logger.error(message, throwable);
            } else {
                logger.error(message);
            }
        }
}
