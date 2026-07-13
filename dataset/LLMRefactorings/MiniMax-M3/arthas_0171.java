public class arthas_0171 {

    @Override
    public void execute(GrpcRequest request, Http2DataFrame frame, ChannelHandlerContext context) throws Throwable {
        final Http2FrameStream frameStream = frame.stream();
        StreamObserver<GrpcResponse> responseObserver = new StreamObserver<GrpcResponse>() {
            AtomicBoolean sendHeader = new AtomicBoolean(false);

            @Override
            public void onNext(GrpcResponse res) {
                // 控制流只能响应一次header
                if (!sendHeader.get()) {
                    sendHeader.compareAndSet(false, true);
                    context.writeAndFlush(new DefaultHttp2HeadersFrame(res.getEndHeader()).stream(frameStream));
                }
                context.writeAndFlush(new DefaultHttp2DataFrame(res.getResponseData()).stream(frameStream));
            }

            @Override
            public void onCompleted() {
                context.writeAndFlush(new DefaultHttp2HeadersFrame(GrpcResponse.getDefaultEndStreamHeader(), true).stream(frameStream));
            }
        };
        try {
            dispatcher.serverStreamExecute(request, responseObserver);
        } catch (Throwable e) {
            throw new RuntimeException(e);
        }
    }
}
