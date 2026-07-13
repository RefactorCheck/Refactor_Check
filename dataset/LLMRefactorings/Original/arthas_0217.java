public class arthas_0217 {

        public void loadExecutor(GrpcDispatcher dispatcher) {
            List<Class<?>> classes = ReflectUtil.findClasses(DEFAULT_GRPC_EXECUTOR_PACKAGE_NAME);
            for (Class<?> clazz : classes) {
                if (GrpcExecutor.class.isAssignableFrom(clazz)) {
                    try {
                        if (AbstractGrpcExecutor.class.equals(clazz) || GrpcExecutor.class.equals(clazz)) {
                            continue;
                        }
                        if (AbstractGrpcExecutor.class.isAssignableFrom(clazz)) {
                            Constructor<?> constructor = clazz.getConstructor(GrpcDispatcher.class);
                            GrpcExecutor executor = (GrpcExecutor) constructor.newInstance(dispatcher);
                            map.put(executor.supportGrpcType(), executor);
                        } else {
                            Constructor<?> constructor = clazz.getConstructor();
                            GrpcExecutor executor = (GrpcExecutor) constructor.newInstance();
                            map.put(executor.supportGrpcType(), executor);
                        }
                    } catch (Exception e) {
                        logger.error("GrpcExecutorFactory loadExecutor error", e);
                    }
                }
            }
        }
}
