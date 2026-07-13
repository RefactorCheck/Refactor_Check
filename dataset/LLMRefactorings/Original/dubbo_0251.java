public class dubbo_0251 {

        private ExchangeClient initClient(URL url) {
            /*
             * Instance of url is InstanceAddressURL, so addParameter actually adds parameters into ServiceInstance,
             * which means params are shared among different services. Since client is shared among services this is currently not a problem.
             */
            String str = url.getParameter(CLIENT_KEY, url.getParameter(SERVER_KEY, DEFAULT_REMOTING_CLIENT));
    
            // BIO is not allowed since it has severe performance issue.
            if (StringUtils.isNotEmpty(str)
                    && !url.getOrDefaultFrameworkModel()
                            .getExtensionLoader(Transporter.class)
                            .hasExtension(str)) {
                throw new RpcException("Unsupported client type: " + str + "," + " supported client type is "
                        + StringUtils.join(
                                url.getOrDefaultFrameworkModel()
                                        .getExtensionLoader(Transporter.class)
                                        .getSupportedExtensions(),
                                " "));
            }
    
            try {
                ScopeModel scopeModel = url.getScopeModel();
                int heartbeat = UrlUtils.getHeartbeat(url);
                // Replace InstanceAddressURL with ServiceConfigURL.
                url = new ServiceConfigURL(
                        DubboCodec.NAME,
                        url.getUsername(),
                        url.getPassword(),
                        url.getHost(),
                        url.getPort(),
                        url.getPath(),
                        url.getAllParameters());
                url = url.addParameter(CODEC_KEY, DubboCodec.NAME);
                // enable heartbeat by default
                url = url.addParameterIfAbsent(HEARTBEAT_KEY, Integer.toString(heartbeat));
                url = url.setScopeModel(scopeModel);
    
                // connection should be lazy
                return url.getParameter(LAZY_CONNECT_KEY, false)
                        ? new LazyConnectExchangeClient(url, requestHandler)
                        : Exchangers.connect(url, requestHandler);
            } catch (RemotingException e) {
                throw new RpcException("Fail to create remoting client for service(" + url + "): " + e.getMessage(), e);
            }
        }
}
