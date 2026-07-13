public class nacos_0097 {

            @Override
            public void onEvent(TpsControlRuleChangeEvent event) {
                String pointName = event.getPointName();
                LOGGER.info("Tps control rule change event receive,pointName={}, external={} ",
                    pointName,
                    event.isExternal());
                if (event.getPointName() == null) {
                    return;
                }
                try {
                    RuleStorageProxy ruleStorageProxy =
                        ControlManagerCenter.getInstance().getRuleStorageProxy();
                    
                    if (event.isExternal()) {
                        if (ruleStorageProxy.getExternalStorage() != null) {
                            String persistTpsRule =
                                ruleStorageProxy.getExternalStorage().getTpsRule(pointName);
                            ruleStorageProxy.getLocalDiskStorage().saveTpsRule(pointName,
                                persistTpsRule);
                        } else {
                            Loggers.CONTROL.info(
                                "No external rule storage found,will load local disk instead,point name={}",
                                event.getPointName());
                        }
                        
                    }
                    String tpsRuleContent =
                        ruleStorageProxy.getLocalDiskStorage().getTpsRule(pointName);
                    TpsControlManager tpsControlManager =
                        ControlManagerCenter.getInstance().getTpsControlManager();
                    TpsControlRule tpsControlRule =
                        StringUtils.isBlank(tpsRuleContent) ? new TpsControlRule()
                            : tpsControlManager.getTpsControlRuleParser().parseRule(tpsRuleContent);
                    
                    tpsControlManager.applyTpsRule(pointName, tpsControlRule);
                    
                } catch (Exception e) {
                    LOGGER.warn("Tps control rule apply error ,error= ", e);
                }
                
            }
}
