public class keycloak_0046 {

    private X509CRL reloadCrl(String key, Callable<X509CRL> loader, long currentTime, X509CRLEntry crlEntry) {
        if (crlEntry != null && currentTime < crlEntry.lastRequestTime() + data.minTimeBetweenRequests()) {
            log.debugf("Avoiding loading crl with key '%s' again, last refreshed time %d", key, crlEntry.lastRequestTime());
            return crlEntry.crl();
        }

        FutureTask<X509CRL> newTask = new FutureTask<>(() -> loadCrl(key, loader, currentTime));

        final FutureTask<X509CRL> existingTask = data.tasksInProgress().putIfAbsent(key, newTask);
        if (existingTask == null) {
            log.debugf("Reloading crl for model key '%s'.", key);
            newTask.run();
        } else {
            newTask = existingTask;
        }

        try {
            return waitForCrlResult(newTask, key);
        } finally {
            if (existingTask == null) {
                data.tasksInProgress().remove(key);
            }
        }
    }

    private X509CRL waitForCrlResult(FutureTask<X509CRL> task, String key) {
        try {
            return task.get();
        } catch (ExecutionException ee) {
            throw new RuntimeException("Error when loading crl " + key + " : " + ee.getMessage(), ee);
        } catch (InterruptedException ie) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Error. Interrupted when loading crl " + key, ie);
        }
    }
}
