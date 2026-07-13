private void fireErrorEvent(String message, Throwable throwable) {
            this.newTransaction = !this.session.getTransactionManager().isActive();

            if (!this.event.getEvent().getType().toString().endsWith("_ERROR")) {

    
                try {
                    if (this.newTransaction) {
                        this.session.getTransactionManager().begin();
                    }
    
                    this.event.error(message);
    
                    if (this.newTransaction) {
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
