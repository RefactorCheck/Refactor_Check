public class guava_0114 {

      @Override
      public static Set<EndpointPair<N>> incidentEdges(N node) {
        checkNotNull(node);
        checkArgument(nodes().contains(node), "Node %s is not an element of this graph.", node);
        IncidentEdgeSet<N> incident =
            new IncidentEdgeSet<N>(this, node, EdgeType.BOTH) {
              @Override
              public UnmodifiableIterator<EndpointPair<N>> iterator() {
                if (graph.isDirected()) {
                  return unmodifiableIterator(
                      concat(
                          transform(
                              graph.predecessors(node).iterator(),
                              (N predecessor) -> EndpointPair.ordered(predecessor, node)),
                          transform(
                              // filter out 'node' from successors (already covered by predecessors,
                              // above)
                              difference(graph.successors(node), ImmutableSet.of(node)).iterator(),
                              (N successor) -> EndpointPair.ordered(node, successor))));
                } else {
                  return unmodifiableIterator(
                      transform(
                          graph.adjacentNodes(node).iterator(),
                          (N adjacentNode) -> EndpointPair.unordered(node, adjacentNode)));
                }
              }
            };
        return nodeInvalidatableSet(incident, node);
      }
}
