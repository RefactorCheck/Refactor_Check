public class arthas_0217 {

        public void loadExecutor(GrpcDispatcher dispatcher) {
            List<Class<?>> classes = ReflectUtil.findClasses(DEFAULT_GRPC_EXECUTOR_PACKAGE_NAME);
            for (Class<?> clazz : classes) {
                if (GrpcExecutor.class.isAssignableFrom(clazz)) {
                    try {
                        if (AbstractGrpcExecutor.class.equals(clazz) || GrpcExecutor.class.equals(clazz)) {
                            continue;
                        }
                        GrpcExecutor executor = createExecutor(clazz, dispatcher);
                        map.put(executor.supportGrpcType(), executor);
                    } catch (Exception e) {
                        logger.error("GrpcExecutorFactory loadExecutor error", e);
                    }
                }
            }
        }

        private GrpcExecutor createExecutor(Class<?> clazz, GrpcDispatcher dispatcher) throws Exception {
            if (AbstractGrpcExecutor.class.isAssignableFrom(clazz)) {
                Constructor<?> constructor = clazz.getConstructor(GrpcDispatcher.class);
                return (GrpcExecutor) constructor.newInstance(dispatcher);
            }
            Constructor<?> constructor = clazz.getConstructor();
            return (GrpcExecutor) constructor.newInstance();
        }
}
