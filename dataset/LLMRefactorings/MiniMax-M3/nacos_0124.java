public class nacos_0124 {

        @Override
        public Response onApply(WriteRequest request) {
            final Lock lock = readLock;
            lock.lock();
            try {
                final InstanceStoreRequest instanceRequest =
                    serializer.deserialize(request.getData().toByteArray());
                final DataOperation operation = DataOperation.valueOf(request.getOperation());
                Response errorResponse = processOperation(operation, instanceRequest);
                if (errorResponse != null) {
                    return errorResponse;
                }
                return Response.newBuilder().setSuccess(true).build();
            } catch (Exception e) {
                Loggers.RAFT.warn("Persistent client operation failed. ", e);
                return Response.newBuilder().setSuccess(false)
                    .setErrMsg("Persistent client operation failed. " + e.getMessage()).build();
            } finally {
                lock.unlock();
            }
        }

        private Response processOperation(DataOperation operation, InstanceStoreRequest instanceRequest) {
            switch (operation) {
                case ADD:
                    onInstanceRegister(instanceRequest.service, instanceRequest.instance,
                        instanceRequest.getClientId());
                    return null;
                case DELETE:
                    onInstanceDeregister(instanceRequest.service, instanceRequest.getClientId());
                    return null;
                case CHANGE:
                    if (instanceAndServiceExist(instanceRequest)) {
                        onInstanceRegister(instanceRequest.service, instanceRequest.instance,
                            instanceRequest.getClientId());
                    }
                    return null;
                default:
                    return Response.newBuilder().setSuccess(false)
                        .setErrMsg("unsupport operation : " + operation)
                        .build();
            }
        }
}
