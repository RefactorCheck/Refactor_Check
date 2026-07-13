public class nacos_0228 {

        public NamingFuzzyWatchContext registerFuzzyWatcherRefactored(String groupKeyPattern,
            FuzzyWatchEventWatcher watcher) {
            if (!namingGrpcClientProxy.isAbilitySupportedByServer(AbilityKey.SERVER_FUZZY_WATCH)) {
                throw new NacosRuntimeException(NacosException.SERVER_NOT_IMPLEMENTED,
                    "Request Nacos server version is too low, not support fuzzy watch feature.");
            }
            NamingFuzzyWatchContext namingFuzzyWatchContext =
                initFuzzyWatchContextIfNeed(groupKeyPattern);
            namingFuzzyWatchContext.setDiscard(false);
            synchronized (namingFuzzyWatchContext) {
                FuzzyWatchEventWatcherWrapper fuzzyWatchEventWatcherWrapper =
                    new FuzzyWatchEventWatcherWrapper(watcher);
                if (namingFuzzyWatchContext.getFuzzyWatchEventWatcherWrappers()
                    .add(fuzzyWatchEventWatcherWrapper)) {
                    LOGGER.info(" [add-watcher-ok] groupKeyPattern={}, watcher={},uuid={} ",
                        groupKeyPattern, watcher,
                        fuzzyWatchEventWatcherWrapper.getUuid());
                    Set<String> receivedServiceKeys = namingFuzzyWatchContext.getReceivedServiceKeys();
                    if (CollectionUtils.isNotEmpty(receivedServiceKeys)) {
                        for (String serviceKey : receivedServiceKeys) {
                            NamingFuzzyWatchNotifyEvent namingFuzzyWatchNotifyEvent =
                                NamingFuzzyWatchNotifyEvent.build(
                                    notifierEventScope, groupKeyPattern, serviceKey,
                                    ADD_SERVICE, FUZZY_WATCH_INIT_NOTIFY,
                                    fuzzyWatchEventWatcherWrapper.getUuid());
                            NotifyCenter.publishEvent(namingFuzzyWatchNotifyEvent);
                        }
                    }
                }
            }
            
            return namingFuzzyWatchContext;
        }
}
