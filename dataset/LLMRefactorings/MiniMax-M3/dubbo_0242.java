public class dubbo_0242 {

    @Override
    public void setException(Throwable t) {
        try {
            if (responseFuture.isDone()) {
                responseFuture.get().setException(t);
            } else {
                completeWithException(t);
            }
        } catch (Exception e) {
            // This should not happen in normal request process;
            logger.error(
                    PROXY_ERROR_ASYNC_RESPONSE,
                    "",
                    "",
                    "Got exception when trying to fetch the underlying result from AsyncRpcResult.");
            throw new RpcException(e);
        }
    }

    private void completeWithException(Throwable t) {
        AppResponse appResponse = new AppResponse(invocation);
        appResponse.setException(t);
        responseFuture.complete(appResponse);
    }
}
