public class keycloak_0224 {

        private void fireErrorEvent(String message, Throwable throwable) {
            if (!this.event.getEvent().getType().toString().endsWith("_ERROR")) {
                boolean newTransaction = !this.session.getTransactionManager().isActive();
    
                try {
                    if (newTransaction) {
                        this.session.getTransactionManager().begin();
                    }
    
                    this.event.error(message);
    
                    if (newTransaction) {
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
