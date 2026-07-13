public class kafka_0153 {

    @SuppressWarnings("rawtypes")
    <KR> KTable<KR, VOut> build(final Map<KGroupedStreamImpl<K, ?>, Aggregator<? super K, ? super Object, VOut>> groupPatterns,
                                final Initializer<VOut> initializer,
                                final NamedInternal named,
                                final StoreFactory storeFactory,
                                final Serde<KR> keySerde,
                                final Serde<VOut> valueSerde,
                                final String queryableName,
                                final boolean isOutputVersioned) {
        processRepartitions(groupPatterns, storeFactory.storeName(), queryableName);
        final Collection<GraphNode> processors = new ArrayList<>();
        final Collection<KStreamAggProcessorSupplier> parentProcessors = new ArrayList<>();

        int counter = 0;
        for (final Entry<KGroupedStreamImpl<K, ?>, Aggregator<? super K, Object, VOut>> kGroupedStream : groupPatterns.entrySet()) {
            final ProcessorGraphNode<K, ?> aggProcessorNode = createAggregationNode(
                kGroupedStream.getValue(), storeFactory, initializer, named, isOutputVersioned, counter++, parentProcessors);
            processors.add(aggProcessorNode);
            builder.addGraphNode(parentNodes.get(kGroupedStream.getKey()), aggProcessorNode);
        }
        return createTable(processors, parentProcessors, named, keySerde, valueSerde, queryableName, storeFactory.storeName());
    }

    private ProcessorGraphNode<K, ?> createAggregationNode(
            final Aggregator<? super K, Object, VOut> aggregator,
            final StoreFactory storeFactory,
            final Initializer<VOut> initializer,
            final NamedInternal named,
            final boolean isOutputVersioned,
            final int counter,
            final Collection<KStreamAggProcessorSupplier> parentProcessors) {
        final KStreamAggProcessorSupplier<K, ?, K, ?> parentProcessor =
            new KStreamAggregate<>(storeFactory, initializer, aggregator);
        parentProcessors.add(parentProcessor);

        final String kStreamAggProcessorName = named.suffixWithOrElseGet(
            "-cogroup-agg-" + counter,
            builder,
            CogroupedKStreamImpl.AGGREGATE_NAME);
        final ProcessorGraphNode<K, ?> aggProcessorNode =
            new ProcessorGraphNode<>(
                kStreamAggProcessorName,
                new ProcessorParameters<>(parentProcessor, kStreamAggProcessorName)
            );
        aggProcessorNode.setOutputVersioned(isOutputVersioned);
        return aggProcessorNode;
    }
}
