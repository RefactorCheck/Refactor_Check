public class arthas_0041 {

        @Override
        @GrpcMethod(value = "biStream", grpcType = GrpcInvokeTypeEnum.BI_STREAM)
        public StreamObserver<GrpcRequest<ArthasUnittest.ArthasUnittestRequest>> biStream(StreamObserver<GrpcResponse<ArthasUnittest.ArthasUnittestResponse>> observer) {
            return new StreamObserver<GrpcRequest<ArthasUnittest.ArthasUnittestRequest>>() {
                @Override
                public void onNext(GrpcRequest<ArthasUnittest.ArthasUnittestRequest> req) {
                    try {
                        byte[] bytes = req.readData();
                        while (bytes != null && bytes.length != 0) {
                            GrpcResponse<ArthasUnittest.ArthasUnittestResponse> grpcResponse = new GrpcResponse<>();
                            grpcResponse.setService("arthas.grpc.unittest.ArthasUnittestService");
                            grpcResponse.setMethod("biStream");
                            grpcResponse.writeResponseData(ArthasUnittest.ArthasUnittestResponse.parseFrom(bytes));
                            observer.onNext(grpcResponse);
                            bytes = req.readData();
                        }
                    } catch (InvalidProtocolBufferException e) {
                        throw new RuntimeException(e);
                    }
                }
    
                @Override
                public void onCompleted() {
                    observer.onCompleted();
                }
            };
        }
}
