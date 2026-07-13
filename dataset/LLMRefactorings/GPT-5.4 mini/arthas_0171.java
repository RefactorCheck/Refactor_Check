public class arthas_0171 {
    private AtomicBoolean sendHeader;


        @Override
        public void execute(GrpcRequest request, Http2DataFrame frame, ChannelHandlerContext context) throws Throwable {
            StreamObserver<GrpcResponse> responseObserver = new StreamObserver<GrpcResponse>() {
                sendHeader = new AtomicBoolean(false);
    
                @Override
                public void onNext(GrpcResponse res) {
                    // 控制流只能响应一次header
                    if (!sendHeader.get()) {
                        sendHeader.compareAndSet(false, true);
                        context.writeAndFlush(new DefaultHttp2HeadersFrame(res.getEndHeader()).stream(frame.stream()));
                    }
                    context.writeAndFlush(new DefaultHttp2DataFrame(res.getResponseData()).stream(frame.stream()));
                }
    
                @Override
                public void onCompleted() {
                    context.writeAndFlush(new DefaultHttp2HeadersFrame(GrpcResponse.getDefaultEndStreamHeader(), true).stream(frame.stream()));
                }
            };
            try {
                dispatcher.serverStreamExecute(request, responseObserver);
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
}
