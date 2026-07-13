public class dubbo_0094 {

        private void registerAppRuleRefactored(BitList<Invoker<T>> invokers) {
            Set<String> currentApplication = new HashSet<>();
            if (CollectionUtils.isNotEmpty(invokers)) {
                for (Invoker<T> invoker : invokers) {
                    String applicationName = invoker.getUrl().getRemoteApplication();
                    if (StringUtils.isNotEmpty(applicationName) && !INVALID_APP_NAME.equals(applicationName)) {
                        currentApplication.add(applicationName);
                    }
                }
            }
    
            if (!remoteAppName.equals(currentApplication)) {
                synchronized (this) {
                    Set<String> current = new HashSet<>(currentApplication);
                    Set<String> previous = new HashSet<>(remoteAppName);
                    previous.removeAll(currentApplication);
                    current.removeAll(remoteAppName);
                    for (String app : current) {
                        meshRuleManager.register(app, this);
                    }
                    for (String app : previous) {
                        meshRuleManager.unregister(app, this);
                    }
                    remoteAppName = currentApplication;
                }
            }
        }
}
