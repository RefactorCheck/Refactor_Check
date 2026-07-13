public class kafka_0230 {

            @SuppressWarnings("unchecked")
            @Override
            public <R> QueryResult<R> query(
                final Query<R> query,
                final PositionBound positionBound,
                final QueryConfig config
            ) {
                final long start = time.nanoseconds();
                final boolean collectExecutionInfo = config.isCollectExecutionInfo();
                final QueryResult<R> result;

                final QueryHandler<?> handler = queryHandlers.get(query.getClass());
                if (handler == null) {
                    result = wrapped().query(query, positionBound, config);
                    if (collectExecutionInfo) {
                        result.addExecutionInfo("Handled in " + getClass() + " in " + (time.nanoseconds() - start) + "ns");
                    }
                } else {
                    result = ((QueryHandler<R>) handler).apply(
                        query,
                        positionBound,
                        config,
                        this
                    );
                    if (collectExecutionInfo) {
                        result.addExecutionInfo("Handled in " + getClass() + " with serdes " + serdes + " in " + (time.nanoseconds() - start) + "ns");
                    }
                }
                return result;
            }
}
