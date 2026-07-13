public class nacos_0048 {

        protected void initTpsRuleRefactored(String pointName) {
            RuleStorageProxy ruleStorageProxy = RuleStorageProxy.getInstance();
            
            String localRuleContent = ruleStorageProxy.getLocalDiskStorage().getTpsRule(pointName);
            if (StringUtils.isNotBlank(localRuleContent)) {
                Loggers.CONTROL.info("Found local disk tps control rule of {},content ={}", pointName,
                    localRuleContent);
            } else if (ruleStorageProxy.getExternalStorage() != null
                && ruleStorageProxy.getExternalStorage().getTpsRule(pointName) != null) {
                localRuleContent = ruleStorageProxy.getExternalStorage().getTpsRule(pointName);
                if (StringUtils.isNotBlank(localRuleContent)) {
                    Loggers.CONTROL.info("Found external  tps control rule of {},content ={}",
                        pointName, localRuleContent);
                }
            }
            
            if (StringUtils.isNotBlank(localRuleContent)) {
                TpsControlRule tpsLimitRule = tpsControlRuleParser.parseRule(localRuleContent);
                this.applyTpsRule(pointName, tpsLimitRule);
            } else {
                Loggers.CONTROL.info("No tps control rule of {} found,content ={}  ", pointName,
                    localRuleContent);
            }
        }
}
