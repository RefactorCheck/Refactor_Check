public class dubbo_0269 {

        private void init() {
            this.ruleKey = moduleModel.getApplicationModel().getApplicationName() + ".migration";
            this.configuration =
                    moduleModel.modelEnvironment().getDynamicConfiguration().orElse(null);
    
            if (this.configuration != null) {
                logger.info("Listening for migration rules on dataId " + ruleKey + ", group "
                        + DUBBO_SERVICEDISCOVERY_MIGRATION);
                configuration.addListener(ruleKey, DUBBO_SERVICEDISCOVERY_MIGRATION, this);
    
                String rawRule = configuration.getConfig(ruleKey, DUBBO_SERVICEDISCOVERY_MIGRATION);
                if (StringUtils.isEmpty(rawRule)) {
                    rawRule = INIT;
                }
                setRawRule(rawRule);
            } else {
                if (logger.isWarnEnabled()) {
                    logger.warn(
                            REGISTRY_EMPTY_ADDRESS,
                            "",
                            "",
                            "Using default configuration rule because config center is not configured!");
                }
                setRawRule(INIT);
            }
    
            scheduleLocalRuleMigration();
        }

        private void scheduleLocalRuleMigration() {
            String localRawRule = moduleModel.modelEnvironment().getLocalMigrationRule();
            if (!StringUtils.isEmpty(localRawRule)) {
                localRuleMigrationFuture = moduleModel
                        .getApplicationModel()
                        .getFrameworkModel()
                        .getBeanFactory()
                        .getBean(FrameworkExecutorRepository.class)
                        .getSharedScheduledExecutor()
                        .schedule(
                                () -> {
                                    if (this.rawRule.equals(INIT)) {
                                        this.process(new ConfigChangedEvent(null, null, localRawRule));
                                    }
                                },
                                getDelay(),
                                TimeUnit.MILLISECONDS);
            }
        }
}
