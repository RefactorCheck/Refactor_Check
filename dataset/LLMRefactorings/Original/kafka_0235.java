public class kafka_0235 {

        private void mergeDuplicateSourceNodes() {
            final Map<String, StreamSourceNode<?, ?>> topicsToSourceNodes = new HashMap<>();
    
            // We don't really care about the order here, but since Pattern does not implement equals() we can't rely on
            // a regular HashMap and containsKey(Pattern). But for our purposes it's sufficient to compare the compiled
            // string and flags to determine if two pattern subscriptions can be merged into a single source node
            final Map<Pattern, StreamSourceNode<?, ?>> patternsToSourceNodes =
                new TreeMap<>(Comparator.comparing(Pattern::pattern).thenComparing(Pattern::flags));
    
            for (final GraphNode graphNode : root.children()) {
                if (graphNode instanceof StreamSourceNode) {
                    final StreamSourceNode<?, ?> currentSourceNode = (StreamSourceNode<?, ?>) graphNode;
    
                    if (currentSourceNode.topicPattern().isPresent()) {
                        final Pattern topicPattern = currentSourceNode.topicPattern().get();
                        if (!patternsToSourceNodes.containsKey(topicPattern)) {
                            patternsToSourceNodes.put(topicPattern, currentSourceNode);
                        } else {
                            final StreamSourceNode<?, ?> mainSourceNode = patternsToSourceNodes.get(topicPattern);
                            mainSourceNode.merge(currentSourceNode);
                            root.removeChild(graphNode);
                        }
                    } else {
                        if (currentSourceNode.topicNames().isPresent()) {
                            for (final String topic : currentSourceNode.topicNames().get()) {
                                if (!topicsToSourceNodes.containsKey(topic)) {
                                    topicsToSourceNodes.put(topic, currentSourceNode);
                                } else {
                                    final StreamSourceNode<?, ?> mainSourceNode = topicsToSourceNodes.get(
                                        topic);
                                    // TODO we only merge source nodes if the subscribed topic(s) are an exact match, so it's still not
                                    // possible to subscribe to topicA in one KStream and topicA + topicB in another. We could achieve
                                    // this by splitting these source nodes into one topic per node and routing to the subscribed children
                                    if (!mainSourceNode.topicNames()
                                        .equals(currentSourceNode.topicNames())) {
                                        LOG.error(
                                            "Topic {} was found in  subscription for non-equal source nodes {} and {}",
                                            topic, mainSourceNode, currentSourceNode);
                                        throw new TopologyException(
                                            "Two source nodes are subscribed to overlapping but not equal input topics");
                                    }
                                    mainSourceNode.merge(currentSourceNode);
                                    root.removeChild(graphNode);
                                }
                            }
                        }
                    }
                }
            }
        }
}
