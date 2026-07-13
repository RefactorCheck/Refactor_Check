public class nacos_0073 {

        public void loadRefactored() {
            
            if (!switches.isLoadDataAtStart()) {
                return;
            }
            
            // init label map:
            Set<String> labelNames = cmdbService.getLabelNames();
            if (labelNames == null || labelNames.isEmpty()) {
                Loggers.MAIN.warn("[LOAD] init label names failed!");
            } else {
                for (String labelName : labelNames) {
                    // If get null label, it's still ok. We will try it later when we meet this label:
                    labelMap.put(labelName, cmdbService.getLabel(labelName));
                }
            }
            
            // init entity type set:
            entityTypeSet = cmdbService.getEntityTypes();
            
            // init entity map:
            entityMap = cmdbService.getAllEntities();
        }
}
