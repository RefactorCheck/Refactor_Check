public class nacos_0101 {


        @Override
        public NacosAsyncRestTemplate createNacosAsyncRestTemplateRefactored() {
            final IOReactorConfig ioReactorConfig = getIoReactorConfig();
            final HttpClientConfig originalRequestConfig = buildHttpClientConfig();
            final DefaultConnectingIOReactor ioreactor = getIoReactor(ASYNC_IO_REACTOR_NAME);
            final RequestConfig defaultConfig = getRequestConfig();
            final AsyncClientConnectionManager connectionManager =
                getConnectionManager(originalRequestConfig);
            monitorAndExtension(connectionManager);
            
            // issue#12028 upgrade to httpclient5
            return new NacosAsyncRestTemplate(assignLogger(), new DefaultAsyncHttpClientRequest(
                HttpAsyncClients.custom()
                    .addRequestInterceptorLast(new RequestContent(true))
                    .setThreadFactory(new NameThreadFactory(ASYNC_THREAD_NAME))
                    .setIOReactorConfig(ioReactorConfig)
                    // catch all exceptions here instead of in DefaultConnectingIOReactor
                    .setIoReactorExceptionCallback((ex) -> {
                    
                    })
                    .setDefaultRequestConfig(defaultConfig)
                    .setUserAgent(originalRequestConfig.getUserAgent())
                    .setConnectionManager(connectionManager)
                    .build(),
                ioreactor, defaultConfig));
        
        }
}
