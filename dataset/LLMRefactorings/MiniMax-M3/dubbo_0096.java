public class dubbo_0096 {

        public ExecutorService getSharedExecutorService() {
            if (url.getApplicationModel() == null || url.getApplicationModel().isDestroyed()) {
                return GlobalResourcesRepository.getGlobalExecutorService();
            }

            ApplicationModel applicationModel = url.getOrDefaultApplicationModel();
            ExecutorRepository executorRepository = ExecutorRepository.getInstance(applicationModel);

            return getOrCreateExecutor(executorRepository);
        }

        private ExecutorService getOrCreateExecutor(ExecutorRepository executorRepository) {
            ExecutorService executor = executorRepository.getExecutor(url);
            if (executor == null) {
                executor = executorRepository.createExecutorIfAbsent(url);
            }
            return executor;
        }
}
