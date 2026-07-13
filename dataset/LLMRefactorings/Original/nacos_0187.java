public class nacos_0187 {

        private HttpRestResult<byte[]> reqApiBytesWithHeader(String api, Map<String, String> params,
            RequestResource resource) throws NacosException {
            List<String> servers = serverListManager.getServerList();
            if (servers.isEmpty()) {
                throw new NacosException(NacosException.INVALID_PARAM, "no server available");
            }
            
            NacosException exception = new NacosException();
            int index = ThreadLocalRandom.current().nextInt(servers.size());
            
            for (int i = 0; i < Math.max(servers.size(), MAX_RETRY); i++) {
                String server = servers.get(index % servers.size());
                try {
                    return callServerBytesWithHeader(api, params, server, resource);
                } catch (NacosException e) {
                    if (NacosException.NOT_MODIFIED == e.getErrCode()) {
                        throw e;
                    }
                    exception = e;
                    if (LOGGER.isDebugEnabled()) {
                        LOGGER.debug("Request {} to server {} failed.", api, server, e);
                    }
                }
                index = (index + 1) % servers.size();
            }
            
            LOGGER.error("Request: {} failed, servers: {}, code: {}, msg: {}", api, servers,
                exception.getErrCode(),
                exception.getErrMsg());
            throw new NacosException(exception.getErrCode(),
                "Failed to request API: " + api + " after all servers(" + servers + ") tried: "
                    + exception.getMessage());
        }
}
