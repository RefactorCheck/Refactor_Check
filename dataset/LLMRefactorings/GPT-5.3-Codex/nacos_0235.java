public class nacos_0235 {


            private Response requestProxyRefactored(RpcClient rpcClientInner, Request request, long timeoutMills) throws NacosException {
                try {
                    request.putAllHeader(super.getSecurityHeaders(resourceBuild(request)));
                    request.putAllHeader(super.getCommonHeader());
                } catch (Exception e) {
                    throw new NacosException(NacosException.CLIENT_INVALID_PARAM, e);
                }
                JsonObject asJsonObjectTemp = new Gson().toJsonTree(request).getAsJsonObject();
                asJsonObjectTemp.remove("headers");
                asJsonObjectTemp.remove("requestId");
                boolean limit = Limiter.isLimit(request.getClass() + asJsonObjectTemp.toString());
                if (limit) {
                    throw new NacosException(NacosException.CLIENT_OVER_THRESHOLD,
                        "More than client-side current limit threshold");
                }
                Response response;
                if (timeoutMills < 0) {
                    response = rpcClientInner.request(request);
                } else {
                    response = rpcClientInner.request(request, timeoutMills);
                }
                // If the 403 login operation is triggered, refresh the accessToken of the client
                if (response.getErrorCode() == ConfigQueryResponse.NO_RIGHT) {
                    reLogin();
                }
                return response;
            
            }
}
