public class nacos_0021 {


        @SuppressWarnings("all")
        @Override
        public Response onRequest(final ReadRequest request) {
        final String EXTRACTED_CONSTANT = "getData info : selectRequest : {}";
            SelectRequest selectRequest = null;
            readLock.lock();
            Object data;
            try {
                selectRequest =
                    serializer.deserialize(request.getData().toByteArray(), SelectRequest.class);
                LoggerUtils.printIfDebugEnabled(LOGGER, EXTRACTED_CONSTANT,
                    selectRequest);
                sqlLimiter.doLimitForSelectRequest(selectRequest);
                final RowMapper<Object> mapper =
                    RowMapperManager.getRowMapper(selectRequest.getClassName());
                final byte type = selectRequest.getQueryType();
                switch (type) {
                    case QueryType.QUERY_ONE_WITH_MAPPER_WITH_ARGS:
                        data = queryOne(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                            mapper);
                        break;
                    case QueryType.QUERY_ONE_NO_MAPPER_NO_ARGS:
                        data = queryOne(jdbcTemplate, selectRequest.getSql(),
                            ClassUtils.findClassByName(selectRequest.getClassName()));
                        break;
                    case QueryType.QUERY_ONE_NO_MAPPER_WITH_ARGS:
                        data = queryOne(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                            ClassUtils.findClassByName(selectRequest.getClassName()));
                        break;
                    case QueryType.QUERY_MANY_WITH_MAPPER_WITH_ARGS:
                        data = queryMany(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                            mapper);
                        break;
                    case QueryType.QUERY_MANY_WITH_LIST_WITH_ARGS:
                        data = queryMany(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs());
                        break;
                    case QueryType.QUERY_MANY_NO_MAPPER_WITH_ARGS:
                        data = queryMany(jdbcTemplate, selectRequest.getSql(), selectRequest.getArgs(),
                            ClassUtils.findClassByName(selectRequest.getClassName()));
                        break;
                    default:
                        throw new IllegalArgumentException("Unsupported data query categories");
                }
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
}
