public class nacos_0203 {

        private void handleClientFuzzyWatchEvent(
            ClientOperationEvent.ClientFuzzyWatchEvent clientFuzzyWatchEvent) {
            String completedPattern = clientFuzzyWatchEvent.getGroupKeyPattern();
            
            //sync fuzzy watch context
            Set<String> patternMatchedServiceKeys =
                namingFuzzyWatchContextService.matchServiceKeys(completedPattern);
            
            Set<String> clientReceivedGroupKeys =
                new HashSet<>(clientFuzzyWatchEvent.getClientReceivedServiceKeys());
            List<FuzzyGroupKeyPattern.GroupKeyState> groupKeyStates =
                FuzzyGroupKeyPattern.diffGroupKeys(
                    patternMatchedServiceKeys, clientReceivedGroupKeys);
            
            // delete notify protection when pattern match count over limit because patternMatchedServiceKeys may not full set.
            if (namingFuzzyWatchContextService.reachToUpLimit(completedPattern)) {
                groupKeyStates.removeIf(item -> !item.isExist());
            }
            
            String syncType =
                clientFuzzyWatchEvent.isInitializing() ? FUZZY_WATCH_INIT_NOTIFY
                    : FUZZY_WATCH_DIFF_SYNC_NOTIFY;
            
            submitSyncTasks(clientFuzzyWatchEvent.getClientId(), completedPattern, syncType,
                convert(groupKeyStates), groupKeyStates);
        }
        
        private void submitSyncTasks(String clientId, String completedPattern, String syncType,
                Set<NamingFuzzyWatchSyncRequest.Context> syncContext,
                List<FuzzyGroupKeyPattern.GroupKeyState> groupKeyStates) {
            if (CollectionUtils.isNotEmpty(groupKeyStates)) {
                Set<Set<NamingFuzzyWatchSyncRequest.Context>> dividedServices =
                    divideServiceByBatch(syncContext);
                BatchTaskCounter batchTaskCounter = new BatchTaskCounter(dividedServices.size());
                int currentBatch = 1;
                for (Set<NamingFuzzyWatchSyncRequest.Context> batchData : dividedServices) {
                    FuzzyWatchSyncNotifyTask fuzzyWatchSyncNotifyTask = new FuzzyWatchSyncNotifyTask(
                        clientId, completedPattern, syncType, batchData,
                        PushConfig.getInstance().getPushTaskRetryDelay());
                    fuzzyWatchSyncNotifyTask.setBatchTaskCounter(batchTaskCounter);
                    fuzzyWatchSyncNotifyTask.setTotalBatch(dividedServices.size());
                    fuzzyWatchSyncNotifyTask.setCurrentBatch(currentBatch);
                    fuzzyWatchPushDelayTaskEngine.addTask(
                        FuzzyWatchPushDelayTaskEngine.getTaskKey(fuzzyWatchSyncNotifyTask),
                        fuzzyWatchSyncNotifyTask);
                    currentBatch++;
                }
            } else if (FUZZY_WATCH_INIT_NOTIFY.equals(syncType)) {
                FuzzyWatchSyncNotifyTask fuzzyWatchSyncNotifyTask = new FuzzyWatchSyncNotifyTask(
                    clientId, completedPattern,
                    FINISH_FUZZY_WATCH_INIT_NOTIFY, null,
                    PushConfig.getInstance().getPushTaskRetryDelay());
                fuzzyWatchPushDelayTaskEngine.addTask(
                    FuzzyWatchPushDelayTaskEngine.getTaskKey(fuzzyWatchSyncNotifyTask),
                    fuzzyWatchSyncNotifyTask);
                
            }
        }
}
