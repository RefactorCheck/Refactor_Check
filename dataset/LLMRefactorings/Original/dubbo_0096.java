public class dubbo_0096 {

        public ExecutorService getSharedExecutorService() {
            // Application may be destroyed before channel disconnected, avoid create new application model
            // see https://github.com/apache/dubbo/issues/9127
            if (url.getApplicationModel() == null || url.getApplicationModel().isDestroyed()) {
                return GlobalResourcesRepository.getGlobalExecutorService();
            }
    
            // note: url.getOrDefaultApplicationModel() may create new application model
            ApplicationModel applicationModel = url.getOrDefaultApplicationModel();
    
            ExecutorRepository executorRepository = ExecutorRepository.getInstance(applicationModel);
    
            ExecutorService executor = executorRepository.getExecutor(url);
    
            if (executor == null) {
                executor = executorRepository.createExecutorIfAbsent(url);
            }
    
            return executor;
        }
}
