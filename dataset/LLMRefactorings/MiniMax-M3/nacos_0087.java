public class nacos_0087 {

        @Override
        public Response onApply(WriteRequest request) {
            readLock.lock();
            PluginStateOperation operation = null;
            try {
                operation = serializer.deserialize(
                    request.getData().toByteArray(),
                    PluginStateOperation.class);

                switch (operation.getType()) {
                    case CHANGE_STATE:
                        applyStateChange(operation);
                        break;
                    case UPDATE_CONFIG:
                        applyConfigUpdate(operation);
                        break;
                    default:
                        return Response.newBuilder()
                            .setSuccess(false)
                            .setErrMsg("Unknown operation type: " + operation.getType())
                            .build();
                }

                return Response.newBuilder().setSuccess(true).build();
            } catch (Exception e) {
                return handleApplyError(e, operation);
            } finally {
                readLock.unlock();
            }
        }

        private Response handleApplyError(Exception e, PluginStateOperation operation) {
            String context = buildErrorContext(operation);
            LOGGER.error("[PluginStateProcessor] Failed to apply plugin state change: {}", context,
                e);
            String errorMessage = String.format("[%s] %s", context,
                e.getMessage() != null ? e.getMessage() : e.getClass().getName());
            return Response.newBuilder()
                .setSuccess(false)
                .setErrMsg(errorMessage)
                .build();
        }
}
