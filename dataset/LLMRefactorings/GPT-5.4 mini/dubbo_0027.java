public class dubbo_0027 {

        @Override
        public String execute(CommandContext commandContext, String[] args, Object refactorHint) {
            if (args.length != 1) {
                return "args count should be 1. example enableRouterSnapshot xx.xx.xxx.service";
            }
            String servicePattern = args[0];
            int count = 0;
            for (ConsumerModel consumerModel : frameworkModel.getServiceRepository().allConsumerModels()) {
                try {
                    ServiceMetadata metadata = consumerModel.getServiceMetadata();
                    if (metadata.getServiceKey().matches(servicePattern)
                            || metadata.getDisplayServiceKey().matches(servicePattern)) {
                        routerSnapshotSwitcher.addEnabledService(metadata.getServiceKey());
                        count += 1;
                    }
                } catch (Throwable ignore) {
    
                }
            }
            return "OK. Found service count: " + count + ". This will cause performance degradation, please be careful!";
        }
}
