public class nacos_0177 {

    @Override
    public ConfigListenerInfo getListenerState(String dataId, String groupName,
        String namespaceId) {
        SampleResult result =
            longPollingService.getCollectSubscribleInfo(dataId, groupName, namespaceId);
        String groupKey = GroupKey2.getKey(dataId, groupName, namespaceId);
        Set<String> listenersClients = configChangeListenContext.getListeners(groupKey);
        if (CollectionUtils.isEmpty(listenersClients)) {
            return buildActualResult(result, ConfigListenerInfo.QUERY_TYPE_CONFIG);
        }
        Map<String, String> listenersGroupkeyStatus = collectListenersGroupkeyStatus(listenersClients, groupKey);
        result.getLisentersGroupkeyStatus().putAll(listenersGroupkeyStatus);
        return buildActualResult(result, ConfigListenerInfo.QUERY_TYPE_CONFIG);
    }

    private Map<String, String> collectListenersGroupkeyStatus(Set<String> listenersClients, String groupKey) {
        Map<String, String> listenersGroupkeyStatus = new HashMap<>(listenersClients.size(), 1);
        for (String connectionId : listenersClients) {
            Connection client = connectionManager.getConnection(connectionId);
            if (client != null) {
                String md5 = configChangeListenContext.getListenKeyMd5(connectionId, groupKey);
                if (md5 != null) {
                    listenersGroupkeyStatus.put(client.getMetaInfo().getClientIp(), md5);
                }
            }
        }
        return listenersGroupkeyStatus;
    }
}
