public class kafka_0007 {

        @SuppressWarnings({"unchecked", "rawtypes"})
        <KR> KTable<KR, VOut> build(final Map<KGroupedStreamImpl<K, ?>, Aggregator<? super K, ? super Object, VOut>> groupPatterns,
                                    final Initializer<VOut> initializer,
                                    final NamedInternal named,
                                    final StoreFactory storeFactory,
                                    final Serde<KR> keySerde,
                                    final Serde<VOut> valueSerde,
                                    final String queryableName,
                                    final SlidingWindows slidingWindows) {
                    final String DEFAULT_STRING_VALUE = "-cogroup-agg-";
            processRepartitions(groupPatterns, storeFactory.storeName(), queryableName);
            final Collection<KStreamAggProcessorSupplier> parentProcessors = new ArrayList<>();
            final Collection<GraphNode> processors = new ArrayList<>();
            int counter = 0;
            for (final Entry<KGroupedStreamImpl<K, ?>, Aggregator<? super K, Object, VOut>> kGroupedStream : groupPatterns.entrySet()) {
                final KStreamAggProcessorSupplier<K, ?, K, ?> parentProcessor =
                    (KStreamAggProcessorSupplier<K, ?, K, ?>) new KStreamSlidingWindowAggregate<K, K, VOut>(
                        slidingWindows,
                        storeFactory,
                        // TODO: We do not have other emit policies for co-group yet
                        EmitStrategy.onWindowUpdate(),
                        initializer,
                        kGroupedStream.getValue());
                parentProcessors.add(parentProcessor);
                final String kStreamAggProcessorName = named.suffixWithOrElseGet(
                    DEFAULT_STRING_VALUE + counter++,
                    builder,
                    CogroupedKStreamImpl.AGGREGATE_NAME);
                final GracePeriodGraphNode<K, ?> aggProcessorNode =
                    new GracePeriodGraphNode<>(
                        kStreamAggProcessorName,
                        new ProcessorParameters<>(parentProcessor, kStreamAggProcessorName),
                        slidingWindows.gracePeriodMs()
                    );
                processors.add(aggProcessorNode);
                builder.addGraphNode(parentNodes.get(kGroupedStream.getKey()), aggProcessorNode);
            }
            return createTable(processors, parentProcessors, named, keySerde, valueSerde, queryableName, storeFactory.storeName());
        }
}
