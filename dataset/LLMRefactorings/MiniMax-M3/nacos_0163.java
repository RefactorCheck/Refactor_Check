public class nacos_0163 {

        @Override
        public ClientSummaryInfo getClientDetail(String clientId) throws NacosApiException {
            Client client = clientManager.getClient(clientId);
            if (null == client) {
                throw new NacosApiException(NacosException.NOT_FOUND, ErrorCode.RESOURCE_NOT_FOUND,
                    String.format("Client id %s not exist.", clientId));
            }
            ClientSummaryInfo result = new ClientSummaryInfo();
            result.setClientId(client.getClientId());
            result.setEphemeral(client.isEphemeral());
            result.setLastUpdatedTime(client.getLastUpdatedTime());
            result.setClientType("ipPort");
            if (client instanceof ConnectionBasedClient) {
                populateConnectionInfo(result, clientId);
            }
            return result;
        }

        private void populateConnectionInfo(ClientSummaryInfo result, String clientId) {
            result.setClientType("connection");
            Connection connection = connectionManager.getConnection(clientId);
            ConnectionMeta connectionMetaInfo = connection.getMetaInfo();
            result.setConnectType(connectionMetaInfo.getConnectType());
            result.setAppName(connectionMetaInfo.getAppName());
            result.setVersion(connectionMetaInfo.getVersion());
            result.setClientIp(connectionMetaInfo.getClientIp());
            result.setClientPort(connectionMetaInfo.getRemotePort());
        }
}
