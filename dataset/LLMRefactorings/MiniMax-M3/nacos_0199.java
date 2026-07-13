public class nacos_0199 {

        public synchronized void applyRule(TpsControlRule newControlRule) {
            Loggers.CONTROL.info("Apply tps control rule start,pointName=[{}]  ", this.getPointName());
            
            if (newControlRule == null || newControlRule.getPointRule() == null) {
                clearRules();
                return;
            }
            
            applyPointRule(newControlRule.getPointRule());
            
            Loggers.CONTROL.info("Apply tps control rule end,pointName=[{}]  ", this.getPointName());
        }

        private void clearRules() {
            Loggers.CONTROL.info("Clear all tps control rule ,pointName=[{}]  ", this.getPointName());
            super.getPointBarrier().clearLimitRule();
        }

        private void applyPointRule(RuleDetail newPointRule) {
            Loggers.CONTROL.info(
                "Update  point  control rule ,pointName=[{}],original maxTps={}, new maxTps={}"
                    + ",original monitorType={}, original monitorType={}, ",
                this.getPointName(),
                this.pointBarrier.getMaxCount(), newPointRule.getMaxCount(),
                this.pointBarrier.getMonitorType(),
                newPointRule.getMonitorType());
            this.pointBarrier.applyRuleDetail(newPointRule);
        }
}
