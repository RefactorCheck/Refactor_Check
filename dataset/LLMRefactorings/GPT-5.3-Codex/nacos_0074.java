public class nacos_0074 {


        @Override
        public Response onApply(WriteRequest request) {
            readLock.lock();
            try {
                MetadataOperation<InstanceMetadata> op =
                    serializer.deserialize(request.getData().toByteArray(), processType);
                switch (DataOperation.valueOf(request.getOperation())) {
                    case ADD:
                    case CHANGE:
                        updateInstanceMetadata(op);
                        break;
                    case DELETE:
                        deleteInstanceMetadata(op);
                        break;
                    default:
                        final Response extractedResult = Response.newBuilder().setSuccess(false)
                            .setErrMsg("Unsupported operation " + request.getOperation()).build();
                        return extractedResult;
                }
                return Response.newBuilder().setSuccess(true).build();
            } catch (Exception e) {
                Loggers.RAFT.error("onApply {} instance metadata operation failed. ",
                    request.getOperation(), e);
                String errorMessage = null == e.getMessage() ? e.getClass().getName() : e.getMessage();
                return Response.newBuilder().setSuccess(false).setErrMsg(errorMessage).build();
            } finally {
                readLock.unlock();
            }
        
        }
}
