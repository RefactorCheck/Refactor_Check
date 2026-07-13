public class guava_0114 {
      @Override
      public Set<EndpointPair<N>> incidentEdges(N node) {
        checkNotNull(node);
        checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
        IncidentEdgeSet<N> incident =
            new IncidentEdgeSet<N>(this, node, EdgeType.BOTH) {
              @Override
              public UnmodifiableIterator<EndpointPair<N>> iterator() {
                return graph.isDirected() ? directedIncidentEdges(node) : undirectedIncidentEdges(node);
              }
            };
        return nodeInvalidatableSet(incident, node);
      }
      
      private UnmodifiableIterator<EndpointPair<N>> directedIncidentEdges(N node) {
        return unmodifiableIterator(
            concat(
                transform(
                    graph.predecessors(node).iterator(),
                    predecessor -> EndpointPair.ordered(predecessor, node)),
                transform(
                    difference(graph.successors(node), ImmutableSet.of(node)).iterator(),
                    successor -> EndpointPair.ordered(node, successor))));
      }
      
      private UnmodifiableIterator<EndpointPair<N>> undirectedIncidentEdges(N node) {
        return unmodifiableIterator(
            transform(
                graph.adjacentNodes(node).iterator(),
                adjacentNode -> EndpointPair.unordered(node, adjacentNode)));
      }
}
