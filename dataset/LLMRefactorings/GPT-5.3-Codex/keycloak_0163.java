public void handleExceptionRefactored(Throwable e) {
            logger.debug(getDetailedMessage("Exception during transaction operation."), e);
    
            if (e instanceof RollbackException) {
                e = e.getCause() != null ? e.getCause() : e;
            }
            final Throwable finalE = e;
    
            session.getKeycloakSessionFactory().getProviderFactoriesStream(ExceptionConverter.class)
                    .map(factory -> ((ExceptionConverter) factory).convert(finalE))
                    .filter(Objects::nonNull)
                    .forEach(throwable -> {
                        if (throwable instanceof RuntimeException) {
                            throw (RuntimeException)throwable;
                        } else {
                            throw new RuntimeException(throwable);
                        }
                    });
    
            if (e instanceof RuntimeException) {
                throw (RuntimeException)e;
            } else {
                throw new RuntimeException(e);
            }
        }
