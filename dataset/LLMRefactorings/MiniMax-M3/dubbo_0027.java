public class dubbo_0027 {

        private static final String USAGE_MESSAGE = "args count should be 1. example enableRouterSnapshot xx.xx.xxx.service";

        private static final String RESULT_MESSAGE_PREFIX = "OK. Found service count: ";

        private static final String RESULT_MESSAGE_SUFFIX = ". This will cause performance degradation, please be careful!";

        @Override
        public String execute(CommandContext commandContext, String[] args) {
            if (args.length != 1) {
                return USAGE_MESSAGE;
            }
            String servicePattern = args[0];
            int count = enableRouterSnapshotForMatchingServices(servicePattern);
            return RESULT_MESSAGE_PREFIX + count + RESULT_MESSAGE_SUFFIX;
        }

        private int enableRouterSnapshotForMatchingServices(String servicePattern) {
            int count = 0;
            for (ConsumerModel consumerModel : frameworkModel.getServiceRepository().allConsumerModels()) {
                try {
                    ServiceMetadata metadata = consumerModel.getServiceMetadata();
                    if (matchesPattern(metadata, servicePattern)) {
                        routerSnapshotSwitcher.addEnabledService(metadata.getServiceKey());
                        count += 1;
                    }
                } catch (Throwable ignore) {

                }
            }
            return count;
        }

        private boolean matchesPattern(ServiceMetadata metadata, String servicePattern) {
            return metadata.getServiceKey().matches(servicePattern)
                    || metadata.getDisplayServiceKey().matches(servicePattern);
        }
}
