public class dubbo_0265 {

        @Override
        public Status check() {
            DataStore dataStore =
                    applicationModel.getExtensionLoader(DataStore.class).getDefaultExtension();
            Map<String, Object> executors = dataStore.get(CommonConstants.EXECUTOR_SERVICE_COMPONENT_KEY);
    
            StringBuilder msg = new StringBuilder();
            Status.Level level = Status.Level.OK;
            for (Map.Entry<String, Object> entry : executors.entrySet()) {
                String port = entry.getKey();
                ExecutorService executor = (ExecutorService) entry.getValue();
                level = processExecutor(executor, port, msg, level);
            }
            return msg.length() == 0 ? new Status(Status.Level.UNKNOWN) : new Status(level, msg.toString());
        }
    
        private Status.Level processExecutor(ExecutorService executor, String port, StringBuilder msg, Status.Level currentLevel) {
            if (!(executor instanceof ThreadPoolExecutor)) {
                return currentLevel;
            }
            ThreadPoolExecutor tp = (ThreadPoolExecutor) executor;
            boolean ok = tp.getActiveCount() < tp.getMaximumPoolSize() - 1;
            Status.Level lvl = Status.Level.OK;
            Status.Level resultLevel = currentLevel;
            if (!ok) {
                resultLevel = Status.Level.WARN;
                lvl = Status.Level.WARN;
            }
    
            if (msg.length() > 0) {
                msg.append(';');
            }
            msg.append("Pool status:")
                    .append(lvl)
                    .append(", max:")
                    .append(tp.getMaximumPoolSize())
                    .append(", core:")
                    .append(tp.getCorePoolSize())
                    .append(", largest:")
                    .append(tp.getLargestPoolSize())
                    .append(", active:")
                    .append(tp.getActiveCount())
                    .append(", task:")
                    .append(tp.getTaskCount())
                    .append(", service port: ")
                    .append(port);
            return resultLevel;
        }
}
