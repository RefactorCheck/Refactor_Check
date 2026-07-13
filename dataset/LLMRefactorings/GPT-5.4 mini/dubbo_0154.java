public static class dubbo_0154 {

        @Override
        public String execute(CommandContext commandContext, String[] args) {
            if (args.length != 1) {
                return "args count should be 1. example getRouterSnapshot xx.xx.xxx.service";
            }
            String servicePattern = args[0];
            StringBuilder stringBuilder = new StringBuilder();
            for (ConsumerModel consumerModel : frameworkModel.getServiceRepository().allConsumerModels()) {
                try {
                    ServiceMetadata metadata = consumerModel.getServiceMetadata();
                    if (metadata.getServiceKey().matches(servicePattern)
                            || metadata.getDisplayServiceKey().matches(servicePattern)) {
                        Object object = metadata.getAttribute(CommonConstants.CURRENT_CLUSTER_INVOKER_KEY);
                        Map<Registry, MigrationInvoker<?>> invokerMap;
                        if (object instanceof Map) {
                            invokerMap = (Map<Registry, MigrationInvoker<?>>) object;
                            for (Map.Entry<Registry, MigrationInvoker<?>> invokerEntry : invokerMap.entrySet()) {
                                Directory<?> directory = invokerEntry.getValue().getDirectory();
                                StateRouter<?> headStateRouter =
                                        directory.getRouterChain().getHeadStateRouter();
                                stringBuilder
                                        .append(metadata.getServiceKey())
                                        .append('@')
                                        .append(Integer.toHexString(System.identityHashCode(metadata)))
                                        .append("\n")
                                        .append("[ All Invokers:")
                                        .append(directory.getAllInvokers().size())
                                        .append(" ] ")
                                        .append("[ Valid Invokers: ")
                                        .append(((AbstractDirectory<?>) directory)
                                                .getValidInvokers()
                                                .size())
                                        .append(" ]\n")
                                        .append("\n")
                                        .append(headStateRouter.buildSnapshot())
                                        .append("\n\n");
                            }
                        }
                    }
                } catch (Throwable ignore) {
    
                }
            }
            return stringBuilder.toString();
        }
}
