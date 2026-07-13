public class dubbo_0004 {

        @Override
        public Future start() {
            synchronized (startLock) {
                if (isStopping() || isStopped() || isFailed()) {
                    throw new IllegalStateException(getIdentifier() + " is stopping or stopped, can not start again");
                }
    
                try {
                    // maybe call start again after add new module, check if any new module
                    boolean hasPendingModule = hasPendingModule();
    
                    if (isStarting()) {
                        // currently, is starting, maybe both start by module and application
                        // if it has new modules, start them
                        if (hasPendingModule) {
                            startModules();
                        }
                        // if it is starting, reuse previous startFuture
                        return startFuture;
                    }
    
                    // if is started and no new module, just return
                    if ((isStarted() || isCompletion()) && !hasPendingModule) {
                        return CompletableFuture.completedFuture(false);
                    }
    
                    // pending -> starting : first start app
                    // started -> starting : re-start app
                    onStarting();
    
                    initialize();
    
                    doStart();
                } catch (Throwable e) {
                    onFailed(getIdentifier() + " start failure", e);
                    throw e;
                }
    
                return startFuture;
            }
        }
}
