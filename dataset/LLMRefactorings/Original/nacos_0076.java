public class nacos_0076 {

        @Override
        public void asyncRequest(Request request, final RequestCallBack requestCallBack)
            throws NacosException {
            Payload grpcRequest = GrpcUtils.convert(request);
            ListenableFuture<Payload> requestFuture = grpcFutureServiceStub.request(grpcRequest);
            
            //set callback .
            Futures.addCallback(requestFuture, new FutureCallback<Payload>() {
                
                @Override
                public void onSuccess(Payload grpcResponse) {
                    if (grpcResponse == null) {
                        requestCallBack.onException(
                            new NacosException(ResponseCode.FAIL.getCode(), "grpc response is null"));
                        return;
                    }
                    Response response = (Response) GrpcUtils.parse(grpcResponse);
                    
                    if (response != null) {
                        if (response instanceof ErrorResponse) {
                            requestCallBack.onException(
                                new NacosException(response.getErrorCode(), response.getMessage()));
                        } else {
                            requestCallBack.onResponse(response);
                        }
                    } else {
                        requestCallBack.onException(
                            new NacosException(ResponseCode.FAIL.getCode(), "response is null"));
                    }
                }
                
                @Override
                public void onFailure(Throwable throwable) {
                    if (throwable instanceof CancellationException) {
                        requestCallBack.onException(
                            new TimeoutException(
                                "Timeout after " + requestCallBack.getTimeout() + " milliseconds."));
                    } else {
                        requestCallBack.onException(throwable);
                    }
                }
            }, requestCallBack.getExecutor() != null ? requestCallBack.getExecutor() : this.executor);
            // set timeout future.
            ListenableFuture<Payload> payloadListenableFuture = Futures.withTimeout(requestFuture,
                requestCallBack.getTimeout(), TimeUnit.MILLISECONDS,
                RpcScheduledExecutor.TIMEOUT_SCHEDULER);
            
        }
}
