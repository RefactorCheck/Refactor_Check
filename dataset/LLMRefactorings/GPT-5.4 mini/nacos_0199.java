public class nacos_0199 {

        public synchronized void applyRuleRefactored(TpsControlRule newControlRule) {
            Loggers.CONTROL.info("Apply tps control rule start,pointName=[{}]  ", this.getPointName());
            
            //1.reset all monitor point for null.
            if (newControlRule == null || newControlRule.getPointRule() == null) {
                Loggers.CONTROL.info("Clear all tps control rule ,pointName=[{}]  ",
                    this.getPointName());
                super.getPointBarrier().clearLimitRule();
                return;
            }
            
            //2.check point rule.
            RuleDetail newPointRule = newControlRule.getPointRule();
            
            Loggers.CONTROL.info(
                "Update  point  control rule ,pointName=[{}],original maxTps={}, new maxTps={}"
                    + ",original monitorType={}, original monitorType={}, ",
                this.getPointName(),
                this.pointBarrier.getMaxCount(), newPointRule.getMaxCount(),
                this.pointBarrier.getMonitorType(),
                newPointRule.getMonitorType());
            this.pointBarrier.applyRuleDetail(newPointRule);
            
            Loggers.CONTROL.info("Apply tps control rule end,pointName=[{}]  ", this.getPointName());
            
        }
}
