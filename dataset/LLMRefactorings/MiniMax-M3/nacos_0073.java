public class nacos_0073 {

        public void load() {
            
            if (!switches.isLoadDataAtStart()) {
                return;
            }
            
            initLabelMap();
            
            entityTypeSet = cmdbService.getEntityTypes();
            
            entityMap = cmdbService.getAllEntities();
        }

        private void initLabelMap() {
            Set<String> labelNames = cmdbService.getLabelNames();
            if (labelNames == null || labelNames.isEmpty()) {
                Loggers.MAIN.warn("[LOAD] init label names failed!");
            } else {
                for (String labelName : labelNames) {
                    labelMap.put(labelName, cmdbService.getLabel(labelName));
                }
            }
        }
}
