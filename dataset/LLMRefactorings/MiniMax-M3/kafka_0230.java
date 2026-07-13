public class kafka_0230 {

            @SuppressWarnings("unchecked")
            @Override
            public <R> QueryResult<R> query(
                final Query<R> query,
                final PositionBound positionBound,
                final QueryConfig config
            ) {
                final long start = time.nanoseconds();
                final QueryResult<R> result;
    
                final QueryHandler<?> handler = queryHandlers.get(query.getClass());
                if (handler == null) {
                    result = wrapped().query(query, positionBound, config);
                    addExecutionInfo(result, config, start, "");
                } else {
                    result = ((QueryHandler<R>) handler).apply(
                        query,
                        positionBound,
                        config,
                        this
                    );
                    addExecutionInfo(result, config, start, " with serdes " + serdes);
                }
                return result;
            }

            private <R> void addExecutionInfo(
                final QueryResult<R> result,
                final QueryConfig config,
                final long start,
                final String suffix
            ) {
                if (config.isCollectExecutionInfo()) {
                    result.addExecutionInfo("Handled in " + getClass() + suffix + " in " + (time.nanoseconds() - start) + "ns");
                }
            }
}
