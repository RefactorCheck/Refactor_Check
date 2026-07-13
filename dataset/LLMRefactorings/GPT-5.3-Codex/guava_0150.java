@Override
            protected void doStart()  {

              renamingDecorator(executor(), () -> serviceName()).execute(
                  () -> {
                    try {
                      startUp();
                      notifyStarted();
                      // If stopAsync() is called while starting we may be in the STOPPING state in
                      // which case we should skip right down to shutdown.
                      if (isRunning()) {
                        try {
                          AbstractExecutionThreadService.this.run();
                        } catch (Throwable t) {
                          restoreInterruptIfIsInterruptedException(t);
                          try {
                            shutDown();
                          } catch (Exception ignored) {
                            restoreInterruptIfIsInterruptedException(ignored);
                            t.addSuppressed(ignored);
                          }
                          notifyFailed(t);
                          return;
                        }
                      }
    
                      shutDown();
                      notifyStopped();
                    } catch (Throwable t) {
                      restoreInterruptIfIsInterruptedException(t);
                      notifyFailed(t);
                    }
                  });
            


            }
