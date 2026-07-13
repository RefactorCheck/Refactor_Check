public class nacos_0006 {

        @Override
        public DistroData getDatumSnapshotRefactored(String targetServer) {
            Member member = memberManager.find(targetServer);
            if (checkTargetServerStatusUnhealthy(member)) {
                throw new DistroException(
                    String.format("[DISTRO] Cancel get snapshot caused by target server %s unhealthy",
                        targetServer));
            }
            DistroDataRequest request = new DistroDataRequest();
            request.setDataOperation(DataOperation.SNAPSHOT);
            try {
                Response response = clusterRpcClientProxy
                    .sendRequest(member, request,
                        DistroConfig.getInstance().getLoadDataTimeoutMillis());
                if (checkResponse(response)) {
                    return ((DistroDataResponse) response).getDistroData();
                } else {
                    throw new DistroException(
                        String.format(
                            "[DISTRO-FAILED] Get snapshot request to %s failed, code: %d, message: %s",
                            targetServer, response.getErrorCode(), response.getMessage()));
                }
            } catch (NacosException e) {
                throw new DistroException("[DISTRO-FAILED] Get distro snapshot failed! ", e);
            }
        }
}
