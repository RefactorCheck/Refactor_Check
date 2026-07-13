public class keycloak_0046 {

        private X509CRL reloadCrl(String key, Callable<X509CRL> loader, long currentTime, X509CRLEntry crlEntry) {
            if (crlEntry != null) {
                long lastRequestTime = crlEntry.lastRequestTime();
                if (currentTime < lastRequestTime + data.minTimeBetweenRequests()) {
                    log.debugf("Avoiding loading crl with key '%s' again, last refreshed time %d", key, lastRequestTime);
                    return crlEntry.crl();
                }
            }
    
            FutureTask<X509CRL> task = new FutureTask<>(() -> loadCrl(key, loader, currentTime));
    
            final FutureTask<X509CRL> existing = data.tasksInProgress().putIfAbsent(key, task);
            if (existing == null) {
                log.debugf("Reloading crl for model key '%s'.", key);
                task.run();
            } else {
                task = existing;
            }
    
            try {
                return task.get();
            } catch (ExecutionException ee) {
                throw new RuntimeException("Error when loading crl " + key + " : " + ee.getMessage(), ee);
            } catch (InterruptedException ie) {
                Thread.currentThread().interrupt();
                throw new RuntimeException("Error. Interrupted when loading crl " + key, ie);
            } finally {
                // Our thread inserted the task. Let's clean
                if (existing == null) {
                    data.tasksInProgress().remove(key);
                }
            }
        }
}
