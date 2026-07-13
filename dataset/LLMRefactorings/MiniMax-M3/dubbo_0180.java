public class dubbo_0180 {

        @Override
        public <T> Exporter<T> export(Invoker<T> invoker) throws RpcException {
            checkDestroyed();
            URL url = invoker.getUrl();
    
            // export service.
            String key = serviceKey(url);
            DubboExporter<T> exporter = new DubboExporter<>(invoker, key, exporterMap);
    
            // export a stub service for dispatching event
            checkStubSupportEvent(url);
    
            openServer(url);
            optimizeSerialization(url);
    
            return exporter;
        }

        private void checkStubSupportEvent(URL url) {
            boolean isStubSupportEvent = url.getParameter(STUB_EVENT_KEY, DEFAULT_STUB_EVENT);
            boolean isCallbackService = url.getParameter(IS_CALLBACK_SERVICE, false);
            if (isStubSupportEvent && !isCallbackService) {
                String stubServiceMethods = url.getParameter(STUB_EVENT_METHODS_KEY);
                if (stubServiceMethods == null || stubServiceMethods.length() == 0) {
                    if (logger.isWarnEnabled()) {
                        logger.warn(
                                PROTOCOL_UNSUPPORTED,
                                "",
                                "",
                                "consumer [" + url.getParameter(INTERFACE_KEY)
                                        + "], has set stub proxy support event ,but no stub methods founded.");
                    }
                }
            }
        }
}
