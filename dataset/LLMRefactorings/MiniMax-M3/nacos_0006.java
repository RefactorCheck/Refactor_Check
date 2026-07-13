public class nacos_0006 {

        @Override
        public DistroData getDatumSnapshot(String targetServer) {
            Member member = memberManager.find(targetServer);
            if (checkTargetServerStatusUnhealthy(member)) {
                throw new DistroException(
                    String.format("[DISTRO] Cancel get snapshot caused by target server %s unhealthy",
                        targetServer));
            }
            return sendSnapshotRequest(targetServer, member);
        }

        private DistroData sendSnapshotRequest(String targetServer, Member member) {
            DistroDataRequest request = new DistroDataRequest();
            request.setDataOperation(DataOperation.SNAPSHOT);
            try {
                Response response = clusterRpcClientProxy
                    .sendRequest(member, request,
                        DistroConfig.getInstance().getLoadDataTimeoutMillis());
                return processSnapshotResponse(response, targetServer);
            } catch (NacosException e) {
                throw new DistroException("[DISTRO-FAILED] Get distro snapshot failed! ", e);
            }
        }

        private DistroData processSnapshotResponse(Response response, String targetServer) {
            if (checkResponse(response)) {
                return ((DistroDataResponse) response).getDistroData();
            } else {
                throw new DistroException(
                    String.format(
                        "[DISTRO-FAILED] Get snapshot request to %s failed, code: %d, message: %s",
                        targetServer, response.getErrorCode(), response.getMessage()));
            }
        }
}
