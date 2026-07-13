public class dubbo_0084 {

        private static void handleHttp2(HttpServletRequest request, HttpServletResponse response) {
            AsyncContext context = request.startAsync(request, response);
            ServletStreamChannel channel = new ServletStreamChannel(request, response, context);
            try {
                Http2TransportListener listener = determineHttp2ServerTransportListenerFactory(request.getContentType())
                        .newInstance(channel, ServletExchanger.getUrl(), FrameworkModel.defaultModel());
    
                boolean isGrpc = listener instanceof GrpcHttp2ServerTransportListener;
                channel.setGrpc(isGrpc);
                context.setTimeout(resolveTimeout(request, isGrpc));
                context.addListener(new TripleAsyncListener(channel));
                ServletInputStream is = request.getInputStream();
                is.setReadListener(new TripleReadListener(listener, channel, is));
                response.getOutputStream().setWriteListener(new TripleWriteListener(channel));
    
                listener.onMetadata(new HttpMetadataAdapter(request));
            } catch (Throwable t) {
                LOGGER.info("Failed to process request", t);
                channel.writeError(Code.UNKNOWN.code, t);
            }
        }
}
