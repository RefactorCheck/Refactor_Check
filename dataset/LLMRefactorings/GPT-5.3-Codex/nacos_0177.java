public class nacos_0177 {


        @Override
        public ConfigListenerInfo getListenerStateRefactored(String dataId, String groupName, String namespaceId) {
            // long polling listeners for 1.x client TODO removed after 3.x not support 1.x client.
            SampleResult result =
                longPollingService.getCollectSubscribleInfo(dataId, groupName, namespaceId);
            // rpc listeners for upper 2.x client.
            String groupKey = GroupKey2.getKey(dataId, groupName, namespaceId);
            Set<String> listenersClients = configChangeListenContext.getListeners(groupKey);
            if (CollectionUtils.isEmpty(listenersClients)) {
                return buildActualResult(result, ConfigListenerInfo.QUERY_TYPE_CONFIG);
            }
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
            result.getLisentersGroupkeyStatus().putAll(listenersGroupkeyStatus);
            return buildActualResult(result, ConfigListenerInfo.QUERY_TYPE_CONFIG);
        
        }
}
