public class nacos_0021 {

        @SuppressWarnings("all")
        @Override
        public Response onRequest(final ReadRequest request) {
            SelectRequest selectRequest = null;
            readLock.lock();
            Object data;
            try {
                selectRequest =
                    serializer.deserialize(request.getData().toByteArray(), SelectRequest.class);
                LoggerUtils.printIfDebugEnabled(LOGGER, "getData info : selectRequest : {}",
                    selectRequest);
                sqlLimiter.doLimitForSelectRequest(selectRequest);
                final RowMapper<Object> mapper =
                    RowMapperManager.getRowMapper(selectRequest.getClassName());
                final byte type = selectRequest.getQueryType();
                data = executeQuery(type, selectRequest, mapper);
                ByteString bytes =
                    data == null ? ByteString.EMPTY : ByteString.copyFrom(serializer.serialize(data));
                return Response.newBuilder().setSuccess(true).setData(bytes).build();
            } catch (Exception e) {
                LOGGER.error("There was an error querying the data, request : {}, error : {}",
                    selectRequest, e.toString());
                return Response.newBuilder().setSuccess(false)
                    .setErrMsg(
                        ClassUtils.getSimplaName(e) + ":" + ExceptionUtil.getCause(e).getMessage())
                    .build();
            } finally {
                readLock.unlock();
            }
        }

        private Object executeQuery(final byte type, final SelectRequest selectRequest,
                                    final RowMapper<Object> mapper) {
            switch (type) {
                case QueryType.QUERY_ONE_WITH_MAPPER_WITH_ARGS:
                    return queryOne(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                        mapper);
                case QueryType.QUERY_ONE_NO_MAPPER_NO_ARGS:
                    return queryOne(jdbcTemplate, selectRequest.getSql(),
                        ClassUtils.findClassByName(selectRequest.getClassName()));
                case QueryType.QUERY_ONE_NO_MAPPER_WITH_ARGS:
                    return queryOne(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                        ClassUtils.findClassByName(selectRequest.getClassName()));
                case QueryType.QUERY_MANY_WITH_MAPPER_WITH_ARGS:
                    return queryMany(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                        mapper);
                case QueryType.QUERY_MANY_WITH_LIST_WITH_ARGS:
                    return queryMany(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs());
                case QueryType.QUERY_MANY_NO_MAPPER_WITH_ARGS:
                    return queryMany(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                        ClassUtils.findClassByName(selectRequest.getClassName()));
                default:
                    throw new IllegalArgumentException("Unsupported data query categories");
            }
        }
}
